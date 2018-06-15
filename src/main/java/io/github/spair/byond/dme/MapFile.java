package io.github.spair.byond.dme;

import lombok.Data;

@Data
@SuppressWarnings("WeakerAccess")
public class MapFile {

    private String name;
    private String path;

    public MapFile(final String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Map file path could not be empty");
        }

        this.path = path;
        String[] splittedPath = path.split("[/\\\\]");
        this.name = splittedPath[splittedPath.length - 1];
    }
}
