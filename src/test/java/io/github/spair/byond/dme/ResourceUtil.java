package io.github.spair.byond.dme;

import java.io.File;
import java.util.Objects;

public final class ResourceUtil {

    public static File readResourceFile(final String path) {
        return new File(Objects.requireNonNull(ResourceUtil.class.getClassLoader().getResource(path)).getFile());
    }
}
