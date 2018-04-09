package io.github.spair.byond.dme;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PostParserTest {

    @Test
    public void testParse() {
        Dme dme = prepareDme();
        new PostParser(dme).parse();

        DmeItem datum = dme.getItem("/datum");
        assertEquals("456", datum.getVar("datumVar"));
        assertEquals(1, datum.getVars().size());
        assertEquals(5, datum.getSubtypes().size());
        assertTrue(datum.getSubtypes().contains("/atom"));
        assertTrue(datum.getSubtypes().contains("/atom/child"));
        assertTrue(datum.getSubtypes().contains("/atom/child/grandchild"));
        assertTrue(datum.getSubtypes().contains("/atom/intermediate"));
        assertTrue(datum.getSubtypes().contains("/atom/intermediate/child"));

        DmeItem atom = dme.getItem("/atom");
        assertEquals("value1", atom.getVar("var1"));
        assertEquals("\"We have TEXT in the NORTH dir\"", atom.getVar("textVar"));
        assertEquals("Some TEXT goes here", atom.getVar("macrosVar"));
        assertEquals("Current dir is 1", atom.getVar("globalVar"));
        assertEquals("456", atom.getVar("datumVar"));
        assertEquals(5, atom.getVars().size());
        assertEquals("/datum", atom.getParentPath());
        assertEquals(4, atom.getSubtypes().size());
        assertTrue(atom.getSubtypes().contains("/atom/child"));
        assertTrue(atom.getSubtypes().contains("/atom/child/grandchild"));
        assertTrue(atom.getSubtypes().contains("/atom/intermediate"));
        assertTrue(atom.getSubtypes().contains("/atom/intermediate/child"));

        assertNotNull(dme.getItem("/atom/intermediate"));

        DmeItem child = dme.getItem("/atom/child");
        assertEquals("456", child.getVar("datumVar"));
        assertEquals("value2", child.getVar("var2"));

        DmeItem grandchild = dme.getItem("/atom/child/grandchild");
        assertEquals("value2-3", grandchild.getVar("var1"));
        assertEquals("value2", grandchild.getVar("var2"));
    }

    private Dme prepareDme() {
        Dme dme = new Dme();
        dme.addMacros("TEXT", "Text");

        DmeItem global = new DmeItem(ByondTypes.GLOBAL);
        global.setNumberVar("NORTH", 1);
        dme.addItem(global);

        DmeItem datum = new DmeItem("/datum");
        datum.setVar("datumVar", "456");
        dme.addItem(datum);

        DmeItem atom = new DmeItem("/atom");
        atom.setVar("var1", "value1");
        atom.setVar("macrosVar", "Some TEXT goes here");
        atom.setVar("globalVar", "Current dir is NORTH");
        atom.setVar("textVar", "\"We have TEXT in the NORTH dir\"");
        dme.addItem(atom);

        DmeItem atomChild = new DmeItem("/atom/child");
        atomChild.setVar("var2", "value2");
        dme.addItem(atomChild);

        DmeItem atomGrandchild = new DmeItem("/atom/child/grandchild");
        atomGrandchild.setVar("var1", "value2-3");
        dme.addItem(atomGrandchild);

        DmeItem intermediate = new DmeItem("/atom/intermediate/child");
        dme.addItem(intermediate);

        return dme;
    }
}