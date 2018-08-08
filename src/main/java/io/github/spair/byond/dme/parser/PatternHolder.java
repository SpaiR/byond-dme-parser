package io.github.spair.byond.dme.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PatternHolder {

    private static final Pattern DIRECTIVES = Pattern.compile("#(ifdef|ifndef|undef|if)[\\s]+(.+)");
    private static final Pattern INCLUDE = Pattern.compile("#include\\s+\"(.*(?:\\.dm|\\.dme|\\.dmm))\"");
    private static final Pattern DEFINE = Pattern.compile("^#define\\s+(\\w+)(?:\\([^)]*\\))?(?:\\s+(.+))?");
    private static final Pattern VAR_DEFINITION = Pattern.compile(
            "^[/\\w]+(?:var(?:/[\\w/]+)?)?/(\\w+)\\s*=\\s*(.+)|^[/\\w]+(?:var(?:/[\\w/]+)?)/(\\w+)");

    static Matcher directivesMatcher(final String text) {
        return DIRECTIVES.matcher(text);
    }

    static Matcher includeMatcher(final String text) {
        return INCLUDE.matcher(text);
    }

    static Matcher defineMatcher(final String text) {
        return DEFINE.matcher(text);
    }

    static Matcher varDefMatcher(final String text) {
        return VAR_DEFINITION.matcher(text);
    }

    private PatternHolder() {
    }
}
