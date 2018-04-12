package io.github.spair.byond.dme;

import org.junit.Test;

import static org.junit.Assert.*;

public class DmeParserTest {

    @Test
    public void testParse() {
        Dme dme = DmeParser.parse(ResourceUtil.loadFile("test_dme.dme"));

        DmeItem item = dme.getItem("/obj/item");
        assertNotNull(item);
        assertEquals("1", item.getVar("custom_var"));
        assertEquals("/obj", item.getParentPath());
        assertTrue(item.getSubtypes().isEmpty());

        assertTrue(!dme.getMacroses().isEmpty());
        assertTrue(!dme.getGlobalObject().getVars().isEmpty());

        assertTrue(!dme.getMapFiles().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithWrongFile() {
        DmeParser.parse(ResourceUtil.loadFile("test_file.txt"));
    }
}