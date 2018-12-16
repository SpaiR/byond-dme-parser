package io.github.spair.byond.dme.parser;

import lombok.val;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class WordReplacer {

    private static final Pattern WORD = Pattern.compile("(?<![\\w\"/])\\w+(?![\\w\"/])");

    // Parse every word in provided text and replace it with value from 'searchMap' if key for it found.
    static String replace(final String fullText, final Map<String, String> searchMap) {
        if (!isStringValue(fullText)) {
            Matcher m = WORD.matcher(fullText);
            StringBuffer output = new StringBuffer();

            while (m.find()) {
                val parsedWord = m.group(0);
                m.appendReplacement(output, searchMap.getOrDefault(parsedWord, parsedWord));
            }

            return m.appendTail(output).toString();
        } else {
            return fullText;
        }
    }

    private static boolean isStringValue(final String value) {
        return startsAndEndsWith(value, '"') || startsAndEndsWith(value, '\'');
    }

    private static boolean startsAndEndsWith(final String str, final char c) {
        return str.charAt(0) == c && str.charAt(str.length() - 1) == c;
    }

    private WordReplacer() {
    }
}
