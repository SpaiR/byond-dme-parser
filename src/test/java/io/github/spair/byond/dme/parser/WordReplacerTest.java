package io.github.spair.byond.dme.parser;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WordReplacerTest {

    @Test
    public void testReplace() {
        Map<String, String> searchMap = new HashMap<String, String>() {{
            put("WORD1", "value1");
            put("WORD2", "value2");
        }};
        String fullText = "1 + 3 + 4 = WORD1 and WORD2";

        assertEquals("1 + 3 + 4 = value1 and value2", WordReplacer.replace(fullText, searchMap));
    }

    @Test
    public void testReplaceOnStringValue() {
        Map<String, String> searchMap = new HashMap<String, String>() {{
            put("WORD1", "value1");
            put("icon/path", "123");
        }};
        String fullText = "\"This is string value with WORD1\"";

        assertEquals("\"This is string value with WORD1\"", WordReplacer.replace(fullText, searchMap));

        fullText = "'icon/path'";
        assertEquals("'icon/path'", WordReplacer.replace(fullText, searchMap));
    }
}