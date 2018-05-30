package io.github.spair.byond.dme;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class DmeItemTest {

    private DmeItem item;

    @Before
    public void setUp() {
        item = new DmeItem("/atom", null);
    }

    @Test
    public void testSetEmptyVar() {
        item.setEmptyVar("var");
        assertEquals("null", item.getVar("var").get());
    }

    @Test
    public void testSetQuotedVar() {
        item.setQuotedVar("var", "text");
        assertEquals("\"text\"", item.getVar("var").get());
    }

    @Test
    public void testSetVarWithNumber() {
        item.setVar("var", 13);
        assertEquals("13", item.getVar("var").get());
    }

    @Test
    public void testGetVarUnquoted() {
        item.setVar("var", "\"123\"");
        assertEquals("123", item.getVarUnquoted("var").get());
    }

    @Test
    public void testGetVarAsInt() {
        item.setVar("var", "123");
        assertEquals(123, (int) item.getVarAsInt("var").get());
    }

    @Test
    public void testGetVarAsDouble() {
        item.setVar("var", 123.0d);
        assertEquals(123.0d, item.getVarAsDouble("var").get(), 0);
    }

    @Test
    public void testIsType() {
        Dme dme = new Dme();

        DmeItem atom = dme.getItemOrCreate("/atom");
        DmeItem obj = dme.getItemOrCreate("/obj");

        atom.addSubtype(obj);

        assertTrue(obj.isType("/obj"));
        assertTrue(obj.isType(atom));
        assertFalse(atom.isType(obj));
    }
}