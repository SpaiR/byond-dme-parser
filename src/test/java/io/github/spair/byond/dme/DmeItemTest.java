package io.github.spair.byond.dme;

import org.junit.Test;

import static org.junit.Assert.*;

public class DmeItemTest {

    @Test
    public void testSetEmptyVar() {
        DmeItem item = new DmeItem("/atom", null);
        item.setEmptyVar("variable");

        assertEquals("null", item.getVar("variable"));
    }

    @Test
    public void testSetStringVar() {
        DmeItem item = new DmeItem("/atom", null);
        item.setStringVar("variable", "text");

        assertEquals("\"text\"", item.getVar("variable"));
    }

    @Test
    public void testSetNumberVar() {
        DmeItem item = new DmeItem("/atom", null);
        item.setNumberVar("variable", 13);

        assertEquals("13", item.getVar("variable"));
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