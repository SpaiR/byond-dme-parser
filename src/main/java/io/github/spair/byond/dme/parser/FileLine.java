package io.github.spair.byond.dme.parser;

import lombok.Data;

@Data
final class FileLine {

    private static final int NO_INDENT = -1;

    private String text;
    private int indentLevel;

    private FileLine() {
    }

    static Builder builder() {
        return new Builder();
    }

    boolean hasNoIndent() {
        return indentLevel == NO_INDENT;
    }

    static final class Builder {

        private int indentLevel = NO_INDENT;
        private StringBuilder text = new StringBuilder();

        private Builder() {
        }

        Builder append(final char c) {
            text.append(c);
            return this;
        }

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

        void setIndentLevel(final int indentLevel) {
            this.indentLevel = indentLevel;
        }
    }
}
