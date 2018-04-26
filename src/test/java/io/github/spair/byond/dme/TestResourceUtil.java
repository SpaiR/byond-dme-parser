package io.github.spair.byond.dme;

import java.io.File;
import java.util.Objects;

final class TestResourceUtil {

    static File readResourceFile(final String path) {
        return new File(Objects.requireNonNull(TestResourceUtil.class.getClassLoader().getResource(path)).getFile());
    }
}
