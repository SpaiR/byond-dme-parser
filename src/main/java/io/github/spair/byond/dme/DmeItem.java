package io.github.spair.byond.dme;

import io.github.spair.byond.ByondTypes;
import lombok.Data;
import lombok.ToString;
import lombok.Setter;
import lombok.Getter;
import lombok.AccessLevel;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

@Data
@ToString(exclude = "environment")
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
            result = Objects.nonNull(itemToCompare) && itemToCompare.subtypes.contains(type);
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

    public String getVar(final String name) {
        return vars.get(name);
    }

    /**
     * Method returns variable without first and last character.
     * It's implied that those chars will be single or double quotes.
     *
     * @param name variable name to get
     * @return unwrapped variable
     */
    public String getVarUnquoted(final String name) {
        final String var = vars.get(name);
        return var.substring(1, var.length() - 1);
    }

    public int getVarAsInt(final String name) {
        return Integer.parseInt(vars.get(name));
    }

    public double getVarAsDouble(final String name) {
        return Double.parseDouble(vars.get(name));
    }

    public void addSubtype(final DmeItem item) {
        subtypes.add(item.getType());
    }

    public void addSubtype(final String subtypePath) {
        subtypes.add(subtypePath);
    }
}
