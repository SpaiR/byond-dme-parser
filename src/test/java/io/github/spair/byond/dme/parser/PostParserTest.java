package io.github.spair.byond.dme.parser;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
public class PostParserTest {

    @Test
    public void testDoParse() {
        Dme dme = prepareDme();
        PostParser.parse(dme);

        DmeItem datum = dme.getItem("/datum");
        assertEquals("456", datum.getVar("datumVar").get());
        assertEquals("6.0", datum.getVar("expressionVar").get());
        assertEquals("\"2 + 4\"", datum.getVar("stringVarWithExpression").get());
        assertEquals(3, datum.getVars().size());
        assertEquals(5, datum.getSubtypes().size());
        assertTrue(datum.getSubtypes().contains("/atom"));
        assertTrue(datum.getSubtypes().contains("/atom/child"));
        assertTrue(datum.getSubtypes().contains("/atom/child/grandchild"));
        assertTrue(datum.getSubtypes().contains("/atom/intermediate"));
        assertTrue(datum.getSubtypes().contains("/atom/intermediate/child"));

        DmeItem atom = dme.getItem("/atom");
        assertEquals("value1", atom.getVar("var1").get());
        assertEquals("\"We have TEXT in the NORTH dir\"", atom.getVar("textVar").get());
        assertEquals("Some TEXT goes here", atom.getVar("macrosVar").get());
        assertEquals("Current dir is 1", atom.getVar("globalVar").get());
        assertEquals("456", atom.getVar("datumVar").get());
        assertEquals(7, atom.getVars().size());
        assertEquals("/datum", atom.getParentPath());
        assertEquals(4, atom.getSubtypes().size());
        assertTrue(atom.getSubtypes().contains("/atom/child"));
        assertTrue(atom.getSubtypes().contains("/atom/child/grandchild"));
        assertTrue(atom.getSubtypes().contains("/atom/intermediate"));
        assertTrue(atom.getSubtypes().contains("/atom/intermediate/child"));

        assertNotNull(dme.getItem("/atom/intermediate"));

        DmeItem child = dme.getItem("/atom/child");
        assertEquals("456", child.getVar("datumVar").get());
        assertEquals("value2", child.getVar("var2").get());

        DmeItem grandchild = dme.getItem("/atom/child/grandchild");
        assertEquals("value2-3", grandchild.getVar("var1").get());
        assertEquals("value2", grandchild.getVar("var2").get());
    }

    private Dme prepareDme() {
        Dme dme = new Dme();
        dme.addMacros("TEXT", "Text");

        DmeItem global = new DmeItem(ByondTypes.GLOBAL, dme);
        global.setVar("NORTH", 1);
        dme.addItem(global);

        DmeItem datum = new DmeItem("/datum", dme);
        datum.setVar("datumVar", "456");
        datum.setVar("expressionVar", "2 + 4");
        datum.setQuotedVar("stringVarWithExpression", "2 + 4");
        dme.addItem(datum);

        DmeItem atom = new DmeItem("/atom", dme);
        atom.setVar("var1", "value1");
        atom.setVar("macrosVar", "Some TEXT goes here");
        atom.setVar("globalVar", "Current dir is NORTH");
        atom.setQuotedVar("textVar", "We have TEXT in the NORTH dir");
        dme.addItem(atom);

        DmeItem atomChild = new DmeItem("/atom/child", dme);
        atomChild.setVar("var2", "value2");
        dme.addItem(atomChild);

        DmeItem atomGrandchild = new DmeItem("/atom/child/grandchild", dme);
        atomGrandchild.setVar("var1", "value2-3");
        dme.addItem(atomGrandchild);

        DmeItem intermediate = new DmeItem("/atom/intermediate/child", dme);
        dme.addItem(intermediate);

        return dme;
    }
}