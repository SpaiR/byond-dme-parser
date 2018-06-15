package io.github.spair.byond.dme;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.VarUtil;
import lombok.Data;
import lombok.ToString;
import lombok.Setter;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

@Data
@ToString(exclude = "environment")
@EqualsAndHashCode(exclude = "environment")
@SuppressWarnings("WeakerAccess")
public class DmeItem {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Dme environment;

    private String type;
    private Map<String, String> vars = new HashMap<>();
    private String parentPath = "";
    private Set<String> subtypes = new HashSet<>();

    public DmeItem(final String type, final Dme environment) {
        this.type = type;
        this.environment = environment;
    }

    public boolean isType(final String typeToCompare) {
        boolean result = type.equals(typeToCompare);

        if (!result) {
            DmeItem itemToCompare = environment.getItem(typeToCompare);
            result = itemToCompare != null && itemToCompare.subtypes.contains(type);
        }

        return result;
    }

    public boolean isType(final DmeItem item) {
        return isType(item.getType());
    }

    public void setVar(final String name, final String value) {
        vars.put(name, value);
    }

    public void setVar(final String name, final Number value) {
        vars.put(name, value.toString());
    }

    public void setEmptyVar(final String name) {
        vars.put(name, ByondTypes.NULL);
    }

    /**
     * Method wraps variable in double quotes and then puts it in current item.
     *
     * @param name variable name to add
     * @param value variable value to wrap and add
     */
    public void setQuotedVar(final String name, final Object value) {
        vars.put(name, '"' + value.toString() + '"');
    }

    public Optional<String> getVar(final String name) {
        return VarUtil.optionalNullable(vars.get(name));
    }

    /**
     * Method returns variable without first and last character.
     * It's implied that those chars will be single or double quotes.
     *
     * @param name variable name to get
     * @return unwrapped variable
     */
    public Optional<String> getVarUnquoted(final String name) {
        return VarUtil.optionalUnquoted(vars.get(name));
    }

    public Optional<Integer> getVarAsInt(final String name) {
        return VarUtil.optionalInt(vars.get(name));
    }

    public Optional<Double> getVarAsDouble(final String name) {
        return VarUtil.optionalDouble(vars.get(name));
    }

    public void addSubtype(final DmeItem item) {
        subtypes.add(item.getType());
    }

    public void addSubtype(final String subtypePath) {
        subtypes.add(subtypePath);
    }
}
