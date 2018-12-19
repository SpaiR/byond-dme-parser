package io.github.spair.byond.dme;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.VarWrapper;
import lombok.Data;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.val;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

@Data
@ToString(exclude = {"environment", "varsLookedUp"})
@EqualsAndHashCode(exclude = {"environment", "varsLookedUp"})
@SuppressWarnings("WeakerAccess")
public class DmeItem {

    @Getter(AccessLevel.NONE)
    private final Dme environment;

    private String type;
    private String parentPath = "";

    private final Map<String, String> vars = new HashMap<>();
    private final Set<String> subtypes = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean varsLookedUp = false;

    public DmeItem(final String type, final Dme environment) {
        this.type = type;
        this.environment = environment;
    }

    ///////////////// Types / Subtypes

    public void addSubtype(final DmeItem item) {
        subtypes.add(item.getType());
    }

    public void addSubtype(final String subtypePath) {
        subtypes.add(subtypePath);
    }

    public boolean isType(final String typeToCompare) {
        boolean isEqualTypes = type.equals(typeToCompare);
        if (!isEqualTypes) {
            DmeItem itemToCompare = environment.getItem(typeToCompare);
            isEqualTypes = (itemToCompare != null && itemToCompare.subtypes.contains(type));
        }
        return isEqualTypes;
    }

    public boolean isType(final DmeItem item) {
        return isType(item.getType());
    }

    ///////////////// Variables

    public void setVar(final String name, final String value) {
        vars.put(name, value);
    }

    public void setVarText(final String name, final String value) {
        vars.put(name, '"' + value + '"');
    }

    public void setVarFilePath(final String name, final String value) {
        vars.put(name, "'" + value + "'");
    }

    public void setVar(final String name, final Number value) {
        vars.put(name, value.toString());
    }

    public void setEmptyVar(final String name) {
        vars.put(name, ByondTypes.NULL);
    }

    public Map<String, String> getAllVars() {
        if (!varsLookedUp && !parentPath.isEmpty()) {
            Map<String, String> varsMap = new HashMap<>();
            lookUpVars(varsMap, environment.getItem(parentPath));
            varsMap.forEach(vars::putIfAbsent);
            varsLookedUp = true;
        }
        return vars;
    }

    public String getVar(final String name) {
        return VarWrapper.rawValue(lookupVar(name));
    }

    public Optional<String> getVarText(final String name) {
        return VarWrapper.optionalText(lookupVar(name));
    }

    public Optional<String> getVarFilePath(final String name) {
        return VarWrapper.optionalFilePath(lookupVar(name));
    }

    public Optional<Integer> getVarInt(final String name) {
        return VarWrapper.optionalInt(lookupVar(name));
    }

    public Optional<Double> getVarDouble(final String name) {
        return VarWrapper.optionalDouble(lookupVar(name));
    }

    private String lookupVar(final String name) {
        if (vars.containsKey(name) || parentPath.isEmpty()) {
            return vars.get(name);
        }
        val parentVarVal = environment.getItem(parentPath).lookupVar(name);
        vars.put(name, parentVarVal);
        return parentVarVal;
    }

    private void lookUpVars(final Map<String, String> varsMap, final DmeItem parent) {
        if (parent != null) {
            parent.vars.forEach(varsMap::putIfAbsent);
            if (!parent.varsLookedUp && !parent.parentPath.isEmpty()) {
                lookUpVars(varsMap, environment.getItem(parent.parentPath));
            }
        }
    }
}
