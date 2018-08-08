package io.github.spair.byond.dme.parser;

import io.github.spair.byond.ByondFiles;
import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.regex.Matcher;

@SuppressWarnings("WeakerAccess")
public final class DmeParser {

    private static final String DIRECTIVE_HASH = "#";
    private static final String DIRECTIVE_UNDEF = "undef";
    private static final String DIRECTIVE_IFDEF = "ifdef";
    private static final String DIRECTIVE_IFNDEF = "ifndef";
    private static final String DIRECTIVE_IF = "if";
    private static final String DIRECTIVE_HASHED_ENDIF = "#endif";

    private List<String> pathTree = new ArrayList<>();
    private Dme dme = DmeInitializer.initialize(new Dme());

    public static Dme parse(final File dmeFile) {
        if (dmeFile.isFile() && dmeFile.getName().endsWith(ByondFiles.DME_SUFFIX)) {
            DmeParser parser = new DmeParser();

            parser.dme.setAbsoluteRootPath(dmeFile.getParentFile().getAbsolutePath());
            parser.doParse(dmeFile);
            PostParser.parse(parser.dme);

            return parser.dme;
        } else {
            throw new IllegalArgumentException("Parser only accept '.dme' files");
        }
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private void doParse(final File file) {
        Map<String, String> macroses = dme.getMacroses();

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

                Matcher matcher = PatternHolder.directivesMatcher(lineText);

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
            Matcher varMatcher = PatternHolder.varDefMatcher(fullPath);

            if (varMatcher.find()) {
                final String value = varMatcher.group(2);

                if (value != null) {
                    final String varName = varMatcher.group(1);
                    dmeItem.setVar(varName, WordDefineChecker.check(value, macroses));
                } else {
                    dmeItem.setEmptyVar(varMatcher.group(3));
                }
            }
        }
    }

    private void addNewMacrosValueIfExist(final String lineText) {
        Matcher matcher = PatternHolder.defineMatcher(lineText);

        if (matcher.find() && matcher.group(2) != null) {
            String macrosValue = matcher.group(2).replace("$", "\\$");
            dme.addMacros(matcher.group(1), WordDefineChecker.check(macrosValue, dme.getMacroses()));
        }
    }

    private void parseIncludedFileIfExist(final String lineText, final File currentFile) {
        Matcher matcher = PatternHolder.includeMatcher(lineText);

        if (matcher.find()) {
            String filePath = matcher.group(1).replace('\\', File.separatorChar);
            String fullFilePath = currentFile.getParentFile().getAbsolutePath() + File.separator + filePath;

            if (filePath.endsWith(ByondFiles.DMM_SUFFIX)) {
                dme.addMapFile(fullFilePath);
            } else {
                dme.addIncludedFile(fullFilePath);
                doParse(new File(fullFilePath));
            }
        }
    }

    private void checkPathTreeSize(final int expectedSize) {
        if (pathTree.size() < expectedSize) {
            pathTree.addAll(Collections.nCopies(expectedSize - pathTree.size(), ""));
        }
    }

    private String formFullPath(final FileLine line) {
        checkPathTreeSize(line.getIndentLevel() + 1);

        pathTree.set(line.getIndentLevel(), line.getText());
        StringBuilder fullPath = new StringBuilder();

        for (int i = 0; i < line.getIndentLevel() + 1; i++) {
            String item = pathTree.get(i);

            if (item != null && !item.isEmpty()) {
                if (item.startsWith("/")) {
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

            typeName.append("/").append(pathPart);
        }

        return typeName.length() > 0 ? typeName.toString() : ByondTypes.GLOBAL;
    }

    private boolean notPartOfTypeName(final String item) {
        return item.contains("=") || item.contains("(")
                || "var".equals(item) || "proc".equals(item) || "global".equals(item)
                || "static".equals(item) || "tmp".equals(item) || "verb".equals(item);
    }

    private DmeParser() {
    }
}
