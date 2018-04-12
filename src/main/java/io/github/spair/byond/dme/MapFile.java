package io.github.spair.byond.dme;

import lombok.Data;

import java.util.Objects;

@Data
@SuppressWarnings("WeakerAccess")
public class MapFile {

    private String name;
    private String path;

    public MapFile(final String path) {
        if (Objects.isNull(path) || path.isEmpty()) {
            throw new IllegalArgumentException("Map file path could not be empty");
        }

        this.path = path;
        String[] splittedPath = path.split("[/\\\\]");
        this.name = splittedPath[splittedPath.length - 1];
    }
}
