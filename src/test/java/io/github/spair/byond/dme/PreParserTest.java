package io.github.spair.byond.dme;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class PreParserTest {

    @Test
    public void testParse() {
        PreParser preParser = new PreParser();
        List<FileLine> fileLines = preParser.parse(ResourceUtil.loadFile("preparse_file.dm"));

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