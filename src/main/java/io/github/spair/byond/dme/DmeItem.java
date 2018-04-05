package io.github.spair.byond.dme;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class DmeItem {

    private String type;
    private Map<String, String> vars = new HashMap<>();
    private String parentPath = "";
    private Set<String> subtypes = new HashSet<>();

    public DmeItem(final String type) {
        this.type = type;
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

    public void addSubtype(final String subtypePath) {
        subtypes.add(subtypePath);
    }
}
