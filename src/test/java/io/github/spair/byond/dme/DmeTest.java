package io.github.spair.byond.dme;

import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DmeTest {

    private Dme dme;

    @Before
    public void setUp() {
        dme = new Dme();
    }

    @Test
    public void testMergeWithJsonWithOverride() {
        dme.getItemOrCreate("/obj").setVar("var", 1);
        assertEquals(1, dme.getItem("/obj").getVarInt("var").get().intValue());

        dme.mergeWithJson("{\n" +
                "  \"definitions\": [\n" +
                "    {\n" +
                "      \"type\": \"/obj\",\n" +
                "      \"parent\": \"\",\n" +
                "      \"subtypes\": [],\n" +
                "      \"vars\": {\n" +
                "        \"var\": 2" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        );
        assertEquals(2, dme.getItem("/obj").getVarInt("var").get().intValue());
    }

    @Test
    public void testMergeWithJsonFromString() {
        dme.mergeWithJson("{\n" +
                "  \"macroses\": {\n" +
                "    \"NUMBER_MACROS\": 100,\n" +
                "    \"TEXT_MACROS\": \"{text}one hundred\",\n" +
                "    \"RAW_MACROS\": \"one hundred\"\n" +
                "  },\n" +
                "  \"definitions\": [\n" +
                "    {\n" +
                "      \"type\": \"/obj\",\n" +
                "      \"parent\": \"/datum\",\n" +
                "      \"subtypes\": [],\n" +
                "      \"vars\": {\n" +
                "        \"number_var\": 100,\n" +
                "        \"text_var\": \"{text}one hundred\",\n" +
                "        \"raw_var\": \"one hundred\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        );
        commonAssert();
    }

    @Test
    public void testMergeWithJsonFromFile() {
        dme.mergeWithJson(new File("src/test/resources/test_merge_json.json"));
        commonAssert();
    }

    @Test
    public void testMergeWithJsonFromInputStream() {
        dme.mergeWithJson(getClass().getClassLoader().getResourceAsStream("test_merge_json.json"));
        commonAssert();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void commonAssert() {
        assertEquals(100, dme.getMacrosInt("NUMBER_MACROS").get().longValue());
        assertEquals("\"one hundred\"", dme.getMacros("TEXT_MACROS"));
        assertEquals("one hundred", dme.getMacrosText("TEXT_MACROS").get());
        assertEquals("one hundred", dme.getMacros("RAW_MACROS"));

        val item = dme.getItem("/obj");

        assertNotNull(item);
        assertEquals("/datum", item.getParentPath());
        assertTrue(item.getSubtypes().isEmpty());

        assertEquals(100, item.getVarInt("number_var").get().longValue());
        assertEquals("\"one hundred\"", item.getVar("text_var"));
        assertEquals("one hundred", item.getVarText("text_var").get());
        assertEquals("one hundred", item.getVar("raw_var"));
    }
}