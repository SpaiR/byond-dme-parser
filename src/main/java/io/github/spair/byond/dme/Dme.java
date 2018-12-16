package io.github.spair.byond.dme;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.VarWrapper;
import lombok.Data;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Data
public class Dme {

    private String absoluteRootPath;

    private final Map<String, String> macroses = new HashMap<>();
    private final Map<String, DmeItem> items = new HashMap<>();

    private final List<String> includedFiles = new ArrayList<>();
    private final List<String> mapFiles = new ArrayList<>();

    ///////////////// Macros

    public void addMacros(final String name, final String value) {
        macroses.put(name, value);
    }

    public void addMacros(final String name, final Number value) {
        macroses.put(name, value.toString());
    }

    public void addMacrosText(final String name, final String value) {
        macroses.put(name, '"' + value + '"');
    }

    public void addMacrosFilePath(final String name, final String value) {
        macroses.put(name, "'" + value + "'");
    }

    public String getMacros(final String name) {
        return VarWrapper.rawValue(macroses.get(name));
    }

    public Optional<String> getMacrosText(final String name) {
        return VarWrapper.optionalText(macroses.get(name));
    }

    public Optional<String> getMacrosFilePath(final String name) {
        return VarWrapper.optionalFilePath(macroses.get(name));
    }

    public Optional<Integer> getMacrosInt(final String name) {
        return VarWrapper.optionalInt(macroses.get(name));
    }

    public Optional<Double> getMacrosDouble(final String name) {
        return VarWrapper.optionalDouble(macroses.get(name));
    }

    ///////////////// Included / Map files

    public void addIncludedFile(final String filePath) {
        includedFiles.add(filePath);
    }

    public void addMapFile(final String filePath) {
        mapFiles.add(filePath);
    }

    public void addItem(final DmeItem item) {
        items.put(item.getType(), item);
    }

    ///////////////// Item

    public DmeItem getItemOrCreate(final String type) {
        return items.computeIfAbsent(type, k -> new DmeItem(type, this));
    }

    public DmeItem getItem(final String type) {
        return items.get(type);
    }

    ///////////////// Global vars

    public Map<String, String> getGlobalVars() {
        return items.get(ByondTypes.GLOBAL).getVars();
    }

    public String getGlobalVar(final String name) {
        return VarWrapper.rawValue(getGlobalVars().get(name));
    }

    public Optional<String> getGlobalVarText(final String name) {
        return VarWrapper.optionalText(getGlobalVars().get(name));
    }

    public Optional<String> getGlobalFilePath(final String name) {
        return VarWrapper.optionalFilePath(getGlobalVars().get(name));
    }

    public Optional<Integer> getGlobalVarInt(final String name) {
        return VarWrapper.optionalInt(getGlobalVars().get(name));
    }

    public Optional<Double> getGlobalVarDouble(final String name) {
        return VarWrapper.optionalDouble(getGlobalVars().get(name));
    }
}
