package io.github.spair.byond.dme;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DmeTest {

    private Dme dme;

    @Before
    public void setUp() {
        dme = new Dme();
    }

    @Test
    public void testGetItemOrCreate() {
        assertEquals(0, dme.getItems().size());

        DmeItem item = dme.getItemOrCreate("/atom");
        assertNotNull(item);
        assertEquals(1, dme.getItems().size());
        assertEquals("/atom", item.getType());
    }

    @Test
    public void testGetMacros() {
        dme.addMacros("v", "V");
        assertEquals("V", dme.getMacros("v"));
    }

    @Test
    public void testGetMacrosUnquoted() {
        dme.addMacros("value", "\"name\"");
        assertEquals("name", dme.getMacrosUnquoted("value"));
    }

    @Test
    public void testGetMacrosAsInt() {
        dme.addMacros("value", 1);
        assertEquals(1, dme.getMacrosAsInt("value"));
    }

    @Test
    public void testGetMacrosAsDouble() {
        dme.addMacros("value", 1.0d);
        assertEquals(1.0d, dme.getMacrosAsDouble("value"), 0);
    }
}