package io.github.spair.byond.dme.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class WordDefineChecker {

    private static final Pattern WORD = Pattern.compile("(?<![\\w\"/])\\w+(?![\\w\"/])");

    // Parse every word in provided text and replace it with value from 'searchMap' if key for it found.
    static String check(final String fullText, final Map<String, String> searchMap) {
        if (!isStringValue(fullText)) {
            Matcher m = WORD.matcher(fullText);
            StringBuffer output = new StringBuffer();

            while (m.find()) {
                if (searchMap.containsKey(m.group(0))) {
                    m.appendReplacement(output, searchMap.get(m.group(0)));
                } else {
                    m.appendReplacement(output, m.group(0));
                }
            }

            return m.appendTail(output).toString();
        } else {
            return fullText;
        }
    }

    private static boolean isStringValue(final String value) {
        return startsAndEndsWith(value, "\"") || startsAndEndsWith(value, "'");
    }

    private static boolean startsAndEndsWith(final String str, final String c) {
        return str.startsWith(c) && str.endsWith(c);
    }

    private WordDefineChecker() {
    }
}
