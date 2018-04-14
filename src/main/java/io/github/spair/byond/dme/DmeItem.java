package io.github.spair.byond.dme;

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
@SuppressWarnings({"unused", "WeakerAccess"})
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

    public void setEmptyVar(final String name) {
        vars.put(name, ByondTypes.NULL);
    }

    public void setStringVar(final String name, final Object value) {
        vars.put(name, '"' + value.toString() + '"');
    }

    public void setNumberVar(final String name, final Number value) {
        vars.put(name, value.toString());
    }

    public String getVar(final String name) {
        return vars.get(name);
    }

    public void addSubtype(final DmeItem item) {
        addSubtype(item.getType());
    }

    public void addSubtype(final String subtypePath) {
        subtypes.add(subtypePath);
    }
}
