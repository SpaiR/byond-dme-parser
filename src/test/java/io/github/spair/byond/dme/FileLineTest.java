package io.github.spair.byond.dme;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileLineTest {

    @Test
    public void testBuilder() {
        FileLine.Builder builder = FileLine.builder();

        assertTrue(builder.hasNoIndent());

        builder.append("1").append('2').append("3");
        builder.setIndentLevel(3);

        assertEquals("123", builder.build().getText());
        assertEquals(3, builder.build().getIndentLevel());
        assertFalse(builder.build().hasNoIndent());
    }
}