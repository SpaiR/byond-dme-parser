package io.github.spair.byond.dme.parser;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class DmeInitializerTest {

    @Test
    public void testInitialize() {
        Dme dme = DmeInitializer.initialize(new Dme());

        assertFalse(dme.getItems().isEmpty());

        DmeItem atom = dme.getItem(ByondTypes.ATOM);

        assertEquals("\"neuter\"", atom.getVar("gender"));
        assertEquals("null", atom.getVar("color"));
        assertEquals("255", atom.getVar("alpha"));
        assertEquals("list()", atom.getVar("overlays"));
    }
}