package io.github.spair.byond.dme.parser;

import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.byond.dme.ResourceUtil;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DmeParserTest {

    @Test
    public void testParse() {
        File testDmeFile = ResourceUtil.readResourceFile("test_dme.dme");
        Dme dme = DmeParser.parse(testDmeFile);

        assertEquals(64, dme.getMacroses().size());
        assertEquals(17, dme.getItems().size());
        assertEquals(3, dme.getIncludedFiles().size());
        assertEquals(3, dme.getMapFiles().size());
        assertEquals(77, dme.getGlobalVars().size());

        assertEquals(dme.getAbsoluteRootPath(), testDmeFile.getParent());

        DmeItem item = dme.getItem("/obj/item");

        assertNotNull(item);
        assertEquals("1", item.getVar("custom_var"));
        assertEquals("/obj", item.getParentPath());
        assertEquals("3", item.getVar("layer"));
        assertTrue(item.getSubtypes().isEmpty());

        assertEquals(2, item.getVars().size());
        assertEquals(48, item.getAllVars().size());
        assertEquals(48, item.getVars().size());
    }
}