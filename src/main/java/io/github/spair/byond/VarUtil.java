package io.github.spair.byond;

import java.util.Optional;

public final class VarUtil {

    public static Optional<String> optionalNullable(final String var) {
        return Optional.ofNullable(var);
    }

    public static Optional<String> optionalUnquoted(final String var) {
        return isEmptyVar(var) ? Optional.empty() : Optional.of(var.substring(1, var.length() - 1));
    }

    public static Optional<Integer> optionalInt(final String var) {
        return isEmptyVar(var) ? Optional.empty() : Optional.of(Integer.parseInt(var));
    }

    public static Optional<Double> optionalDouble(final String var) {
        return isEmptyVar(var) ? Optional.empty() : Optional.of(Double.parseDouble(var));
    }

    private static boolean isEmptyVar(final String var) {
        return var == null || ByondTypes.NULL.equals(var) || var.isEmpty();
    }

    private VarUtil() {
    }
}
