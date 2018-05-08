package io.github.spair.byond.dme;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PreParserTest {

    @Test
    public void testParse() {
        List<FileLine> fileLines = PreParser.parse(ResourceUtil.readResourceFile("preparse_file.dm"));

        assertEquals("var/VARIABLE = 123", fileLines.get(0).getText());
        assertEquals(0, fileLines.get(0).getIndentLevel());

        assertEquals("var/VARIABLE2 = 456", fileLines.get(1).getText());
        assertEquals(0, fileLines.get(1).getIndentLevel());

        assertEquals("/obj/item", fileLines.get(2).getText());
        assertEquals(0, fileLines.get(2).getIndentLevel());

        assertEquals("var/obj_var = 789", fileLines.get(3).getText());
        assertEquals(1, fileLines.get(3).getIndentLevel());
    }
}