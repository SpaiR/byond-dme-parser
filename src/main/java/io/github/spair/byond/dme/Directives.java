package io.github.spair.byond.dme;

@SuppressWarnings("unused")
final class Directives {

    static final String HASH = "#";
    static final String ENDIF = "endif";
    static final String UNDEF = "undef";
    static final String IFDEF = "ifdef";
    static final String IFNDEF = "ifndef";
    static final String IF = "if";

    static final class Hashed {
        static final String ENDIF = "#endif";
        static final String UNDEF = "#undef";
        static final String IFDEF = "#ifdef";
        static final String IFNDEF = "#ifndef";
        static final String IF = "#if";

        private Hashed() {
        }
    }

    private Directives() {
    }
}
