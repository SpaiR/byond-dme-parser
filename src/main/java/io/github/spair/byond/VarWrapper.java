package io.github.spair.byond;

import java.util.Optional;

public final class VarWrapper {

    public static String rawValue(final String var) {
        return var == null ? ByondTypes.NULL : var;
    }

    public static Optional<String> optionalText(final String var) {
        if (isEmptyVar(var)) {
            return Optional.empty();
        }
        if (var.startsWith("\"") && var.endsWith("\"")) {
            return Optional.of(var.substring(1, var.length() - 1));
        }
        return Optional.of(var);
    }

    public static Optional<String> optionalFilePath(final String var) {
        if (isEmptyVar(var)) {
            return Optional.empty();
        }
        if (var.startsWith("'") && var.endsWith("'")) {
            return Optional.of(var.substring(1, var.length() - 1));
        }
        return Optional.of(var);
    }

    public static Optional<Integer> optionalInt(final String var) {
        try {
            return isEmptyVar(var) ? Optional.empty() : Optional.of(Integer.parseInt(var));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> optionalDouble(final String var) {
        try {
            return isEmptyVar(var) ? Optional.empty() : Optional.of(Double.parseDouble(var));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> optionalBoolean(final String var) {
        if (var == null) {
            return Optional.empty();
        }
        if (ByondTypes.NULL.equals(var) || var.isEmpty() || "\"\"".equals(var) || "0".equals(var)) {
            return Optional.of(false);
        }
        return Optional.of(true);
    }

    private static boolean isEmptyVar(final String var) {
        return var == null || ByondTypes.NULL.equals(var) || var.isEmpty();
    }

    private VarWrapper() {
    }
}
