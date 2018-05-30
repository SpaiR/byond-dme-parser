package io.github.spair.byond.dme;

import io.github.spair.byond.ByondTypes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("ConstantConditions")
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
        assertEquals("V", dme.getMacros("v").get());
    }

    @Test
    public void testGetMacrosUnquoted() {
        dme.addMacros("value", "\"name\"");
        assertEquals("name", dme.getMacrosUnquoted("value").get());
    }

    @Test
    public void testGetMacrosAsInt() {
        dme.addMacros("value", 1);
        assertEquals(1, (int) dme.getMacrosAsInt("value").get());
    }

    @Test
    public void testGetMacrosAsDouble() {
        dme.addMacros("value", 1.0d);
        assertEquals(1.0d, dme.getMacrosAsDouble("value").get(), 0);
    }

    @Test
    public void testGetGlobalVar() {
        DmeItem global = new DmeItem(ByondTypes.GLOBAL, null);
        global.setVar("value", "name");
        dme.addItem(global);

        assertEquals("name", dme.getGlobalVar("value").get());
    }

    @Test
    public void testGetGlobalVarUnquoted() {
        DmeItem global = new DmeItem(ByondTypes.GLOBAL, null);
        global.setVar("value", "\"name\"");
        dme.addItem(global);

        assertEquals("name", dme.getGlobalVarUnquoted("value").get());
    }

    @Test
    public void testGetGlobalVarAsInt() {
        DmeItem global = new DmeItem(ByondTypes.GLOBAL, null);
        global.setVar("value", "123");
        dme.addItem(global);

        assertEquals(123, (int) dme.getGlobalVarAsInt("value").get());
    }

    @Test
    public void testGetGlobalVarAsDouble() {
        DmeItem global = new DmeItem(ByondTypes.GLOBAL, null);
        global.setVar("value", "123.5");
        dme.addItem(global);

        assertEquals(123.5d, dme.getGlobalVarAsDouble("value").get(), 0);
    }
}