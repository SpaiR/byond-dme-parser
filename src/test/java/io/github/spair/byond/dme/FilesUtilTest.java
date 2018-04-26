package io.github.spair.byond.dme;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FilesUtilTest {

    @Test
    public void testLoadFile() {
        File file = TestResourceUtil.readResourceFile("test_file.txt");

        assertNotNull(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertEquals("test_file.txt", file.getName());
    }
}