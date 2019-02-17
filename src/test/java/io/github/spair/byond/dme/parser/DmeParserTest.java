package io.github.spair.byond.dme.parser;

import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import io.github.spair.byond.dme.ResourceUtil;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class DmeParserTest {

    @Test
    public void testParse() {
        File testDmeFile = ResourceUtil.readResourceFile("test_dme.dme");
        Dme dme = DmeParser.parse(testDmeFile);

        assertEquals(64, dme.getMacroses().size());
        assertEquals(18, dme.getItems().size());
        assertEquals(3, dme.getIncludedFiles().size());
        assertEquals(3, dme.getMapFiles().size());
        assertEquals(77, dme.getGlobalVars().size());

        assertEquals(dme.getAbsoluteRootPath(), testDmeFile.getParent());

        DmeItem datum = dme.getItem("/datum");
        assertEquals(new HashSet<>(Arrays.asList("/image", "/matrix", "/atom", "/exception", "/icon", "/mutable_appearance", "/regex", "/database", "/sound")), datum.getDirectSubtypes());

        DmeItem atom = dme.getItem("/atom");
        assertEquals(new HashSet<>(Arrays.asList("/atom/movable", "/mob", "/area", "/turf", "/obj")), atom.getDirectSubtypes());

        DmeItem item = dme.getItem("/obj/item");

        assertNotNull(item);
        assertEquals("1", item.getVar("custom_var"));
        assertEquals("/obj", item.getParentPath());
        assertEquals("3", item.getVar("layer"));
        assertEquals("0", item.getVar("macros_var"));
        assertTrue(item.getAllSubtypes().isEmpty());
        assertTrue(item.getDirectSubtypes().isEmpty());

        assertEquals(3, item.getVars().size());
        assertEquals(49, item.getAllVars().size());
        assertEquals(49, item.getVars().size());
    }
}