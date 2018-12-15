package io.github.spair.byond.dme.parser;

import io.github.spair.byond.dme.Dme;

import java.io.File;

public final class DmeParser {

    public static Dme parse(final File dmeFile) {
        Parser parser = new Parser(dmeFile);
        parser.parseFile(dmeFile);
        Dme dme = parser.getDme();
        PostParser.parse(dme);
        return dme;
    }

    private DmeParser() {
    }
}
