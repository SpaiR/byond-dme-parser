package io.github.spair.byond.dme;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class WordDefineCheckerTest {

    @Test
    public void testCheck() {
        Map<String, String> searchMap = new HashMap<String, String>() {{
            put("WORD1", "value1");
            put("WORD2", "value2");
        }};
        String fullText = "1 + 3 + 4 = WORD1 and WORD2";

        assertEquals("1 + 3 + 4 = value1 and value2", WordDefineChecker.check(fullText, searchMap));
    }

    @Test
    public void testCheckOnStringValue() {
        Map<String, String> searchMap = new HashMap<String, String>() {{ put("WORD1", "value1"); }};
        String fullText = "\"This is string value with WORD1\"";

        assertEquals("\"This is string value with WORD1\"", WordDefineChecker.check(fullText, searchMap));
    }
}