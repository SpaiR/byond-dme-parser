package io.github.spair.byond.dme.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PatternHolder {

    private final Pattern directives = Pattern.compile("#(ifdef|ifndef|undef|if)[\\s]+(.+)");
    private final Pattern include = Pattern.compile("#include\\s+\"(.*(?:\\.dm|\\.dme|\\.dmm))\"");
    private final Pattern define = Pattern.compile("^#define\\s+(\\w+)(?:\\([^)]*\\))?(?:\\s+(.+))?");
    private final Pattern varDefinition = Pattern.compile(
            "^[/\\w]+(?:var(?:/[\\w/]+)?)?/(\\w+)\\s*=\\s*(.+)|^[/\\w]+(?:var(?:/[\\w/]+)?)/(\\w+)");

    Matcher directivesMatcher(final String text) {
        return directives.matcher(text);
    }

    Matcher includeMatcher(final String text) {
        return include.matcher(text);
    }

    Matcher defineMatcher(final String text) {
        return define.matcher(text);
    }

    Matcher varDefMatcher(final String text) {
        return varDefinition.matcher(text);
    }
}
