package io.github.spair.byond.dme;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapFileTest {

    @Test
    public void testMapFileConstructor() {
        MapFile mapFile = new MapFile("path/to/map.dmm");

        assertEquals("map.dmm", mapFile.getName());
        assertEquals("path/to/map.dmm", mapFile.getPath());

        mapFile = new MapFile("path\\to\\map.dmm");

        assertEquals("map.dmm", mapFile.getName());
        assertEquals("path\\to\\map.dmm", mapFile.getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapFileConstructorException() {
        new MapFile("");
    }
}