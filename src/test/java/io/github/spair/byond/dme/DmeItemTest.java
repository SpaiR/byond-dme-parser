package io.github.spair.byond.dme;

import org.junit.Test;

import static org.junit.Assert.*;

public class DmeItemTest {

    @Test
    public void testSetEmptyVar() {
        DmeItem item = new DmeItem("/atom");
        item.setEmptyVar("variable");

        assertEquals("null", item.getVar("variable"));
    }

    @Test
    public void testSetStringVar() {
        DmeItem item = new DmeItem("/atom");
        item.setStringVar("variable", "text");

        assertEquals("\"text\"", item.getVar("variable"));
    }

    @Test
    public void testSetNumberVar() {
        DmeItem item = new DmeItem("/atom");
        item.setNumberVar("variable", 13);

        assertEquals("13", item.getVar("variable"));
    }
}