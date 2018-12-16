package io.github.spair.byond.dme.parser;

import io.github.spair.byond.ByondFiles;
import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import lombok.Getter;

import java.io.File;
import java.util.Map;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Parser {

    private static final String INITIAL_DME_FILE = "initial_dme.json";

    private static final String DIRECTIVE_HASH = "#";
    private static final String DIRECTIVE_UNDEF = "undef";
    private static final String DIRECTIVE_IFDEF = "ifdef";
    private static final String DIRECTIVE_IFNDEF = "ifndef";
    private static final String DIRECTIVE_IF = "if";
    private static final String DIRECTIVE_HASHED_ENDIF = "#endif";

    private final Pattern directives = Pattern.compile("#(ifdef|ifndef|undef|if)[\\s]+(.+)");
    private final Pattern include = Pattern.compile("#include\\s+\"(.*(?:\\.dm|\\.dme|\\.dmm))\"");
    private final Pattern define = Pattern.compile("^#define\\s+(\\w+)(?:\\([^)]*\\))?(?:\\s+(.+))?");
    private final Pattern varDefinition = Pattern.compile(
            "^[/\\w]+(?:var(?:/[\\w/]+)?)?/(\\w+)\\s*=\\s*(.+)|^[/\\w]+(?:var(?:/[\\w/]+)?)/(\\w+)");

    private String[] pathTree = new String[10];

    @Getter
    private Dme dme;
    private final Map<String, String> macroses;

    Parser(final File dmeFile) {
        dme = new Dme();
        dme.mergeWithJson(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(INITIAL_DME_FILE)));
        dme.setAbsoluteRootPath(dmeFile.getParentFile().getAbsolutePath());
        macroses = dme.getMacroses();
    }

    void parseFile(final File file) {
        Deque<Boolean> preProcessStack = new ArrayDeque<>();
        int preProcessBlocked = 0;

        for (FileLine line : PreParser.parse(file)) {
            final String lineText = line.getText();

            if (line.hasNoIndent()) {
                continue;
            }

            if (lineText.startsWith(DIRECTIVE_HASH)) {
                if (lineText.contains(DIRECTIVE_HASHED_ENDIF) && !preProcessStack.removeLast()) {
                    preProcessBlocked--;
                }

                Matcher matcher = directives.matcher(lineText);

                if (matcher.find()) {
                    final String macrosValue = matcher.group(2);

                    switch (matcher.group(1)) {
                        case DIRECTIVE_UNDEF:
                            macroses.remove(macrosValue);
                            break;
                        case DIRECTIVE_IFDEF:
                            boolean isDefined = macroses.containsKey(macrosValue);
                            preProcessStack.addLast(isDefined);

                            if (!isDefined) {
                                preProcessBlocked++;
                            }

                            break;
                        case DIRECTIVE_IFNDEF:
                            boolean isNotDefined = !macroses.containsKey(macrosValue);
                            preProcessStack.addLast(isNotDefined);

                            if (!isNotDefined) {
                                preProcessBlocked++;
                            }

                            break;
                        case DIRECTIVE_IF:
                            preProcessStack.addLast(true);
                            break;
                    }
                }
                if (preProcessBlocked > 0) {
                    continue;
                }

                addNewMacrosValueIfExist(lineText);
                parseIncludedFileIfExist(lineText, file);

                continue;
            }

            final String fullPath = formFullPath(line);
            final String type = formTypeName(fullPath);

            DmeItem dmeItem = dme.getItemOrCreate(type);
            Matcher varMatcher = varDefinition.matcher(fullPath);

            if (varMatcher.find()) {
                final String value = varMatcher.group(2);

                if (value != null) {
                    final String varName = varMatcher.group(1);
                    dmeItem.setVar(varName, WordReplacer.replace(value, macroses));
                } else {
                    dmeItem.setEmptyVar(varMatcher.group(3));
                }
            }
        }
    }

    private void addNewMacrosValueIfExist(final String lineText) {
        Matcher matcher = define.matcher(lineText);

        if (matcher.find() && matcher.group(2) != null) {
            String macrosValue = matcher.group(2).replace("$", "\\$");
            dme.addMacros(matcher.group(1), WordReplacer.replace(macrosValue, dme.getMacroses()));
        }
    }

    private void parseIncludedFileIfExist(final String lineText, final File currentFile) {
        Matcher matcher = include.matcher(lineText);

        if (matcher.find()) {
            String filePath = matcher.group(1).replace('\\', File.separatorChar);
            String fullFilePath = currentFile.getParentFile().getAbsolutePath() + File.separator + filePath;

            if (filePath.endsWith(ByondFiles.DMM_SUFFIX)) {
                dme.addMapFile(fullFilePath);
            } else {
                dme.addIncludedFile(fullFilePath);
                parseFile(new File(fullFilePath));
            }
        }
    }

    private void checkPathTreeSize(final int expectedSize) {
        if (pathTree.length < expectedSize) {
            String[] newPathTree = new String[expectedSize];
            System.arraycopy(pathTree, 0, newPathTree, 0, pathTree.length);
            pathTree = newPathTree;
        }
    }

    private String formFullPath(final FileLine line) {
        checkPathTreeSize(line.getIndentLevel() + 1);

        pathTree[line.getIndentLevel()] = line.getText();
        StringBuilder fullPath = new StringBuilder();

        for (int i = 0; i < line.getIndentLevel() + 1; i++) {
            String item = pathTree[i];

            if (item != null && !item.isEmpty()) {
                if (item.charAt(0) == '/') {
                    fullPath = new StringBuilder(item);
                } else {
                    fullPath.append('/').append(item);
                }
            }
        }

        return fullPath.toString();
    }

    private String formTypeName(final String fullPath) {
        StringBuilder typeName = new StringBuilder();

        for (String pathPart : fullPath.split("/")) {
            if (pathPart.isEmpty()) {
                continue;
            } else if (notPartOfTypeName(pathPart)) {
                break;
            }

            typeName.append('/').append(pathPart);
        }

        return typeName.length() > 0 ? typeName.toString() : ByondTypes.GLOBAL;
    }

    private boolean notPartOfTypeName(final String pathPart) {
        return pathPart.contains("=") || pathPart.contains("(")
                || "var".equals(pathPart) || "proc".equals(pathPart) || "global".equals(pathPart)
                || "static".equals(pathPart) || "tmp".equals(pathPart) || "verb".equals(pathPart);
    }
}
