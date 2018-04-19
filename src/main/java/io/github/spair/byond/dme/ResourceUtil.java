package io.github.spair.byond.dme;

import java.io.File;
import java.util.Objects;

final class ResourceUtil {

    private static final ClassLoader CLASS_LOADER = ResourceUtil.class.getClassLoader();

    static File loadFile(final String path) {
        return new File(Objects.requireNonNull(CLASS_LOADER.getResource(path)).getFile());
    }

    private ResourceUtil() {
    }
}
