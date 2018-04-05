package io.github.spair.byond.dme;

import lombok.Data;

@Data
class FileLine {

    private static final int NO_INDENT = -1;

    private String text;
    private int indentLevel;

    private FileLine() {
    }

    static Builder builder() {
        return new Builder();
    }

    public boolean hasNoIndent() {
        return indentLevel == NO_INDENT;
    }

    static class Builder {

        private int indentLevel = NO_INDENT;
        private StringBuilder text = new StringBuilder();

        private Builder() {
        }

        Builder append(final char c) {
            text.append(c);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder append(final String s) {
            text.append(s);
            return this;
        }

        FileLine build() {
            FileLine fileLine = new FileLine();
            fileLine.indentLevel = indentLevel;
            fileLine.text = text.toString().trim();
            return fileLine;
        }

        boolean hasNoIndent() {
            return indentLevel == NO_INDENT;
        }

        void setIndentLevel(int indentLevel) {
            this.indentLevel = indentLevel;
        }
    }
}
