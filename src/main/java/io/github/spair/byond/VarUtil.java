package io.github.spair.byond;

import java.util.Objects;
import java.util.Optional;

public final class VarUtil {

    public static Optional<String> optionalNullable(final String var) {
        return Optional.ofNullable(var);
    }

    public static Optional<String> optionalUnquoted(final String var) {
        if (isEmptyVar(var)) {
            return Optional.empty();
        }
        return Optional.of(var.substring(1, var.length() - 1));
    }

    public static Optional<Integer> optionalInt(final String var) {
        if (isEmptyVar(var)) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(var));
    }

    public static Optional<Double> optionalDouble(final String var) {
        if (isEmptyVar(var)) {
            return Optional.empty();
        }
        return Optional.of(Double.parseDouble(var));
    }

    private static boolean isEmptyVar(final String var) {
        return Objects.isNull(var) || ByondTypes.NULL.equals(var) || var.isEmpty();
    }

    private VarUtil() {
    }
}
