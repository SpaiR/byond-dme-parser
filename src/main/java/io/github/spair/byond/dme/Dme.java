package io.github.spair.byond.dme;

import io.github.spair.byond.ByondTypes;
import lombok.Data;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

@Data
@SuppressWarnings("WeakerAccess")
public class Dme {

    private String absoluteRootPath;
    private Map<String, String> macroses = new HashMap<>();
    private List<String> includedFiles = new ArrayList<>();
    private List<MapFile> mapFiles = new ArrayList<>();
    private Map<String, DmeItem> items = new TreeMap<>();

    public void addMacros(final String name, final String value) {
        macroses.put(name, value);
    }

    public void addMacros(final String name, final Number value) {
        macroses.put(name, value.toString());
    }

    public String getMacros(final String name) {
        return macroses.get(name);
    }

    /**
     * Method returns macros value without first and last character.
     * It's implied that those chars will be single or double quotes.
     *
     * @param name macros name to get
     * @return unwrapped macros value
     */
    public String getMacrosUnquoted(final String name) {
        final String wrappedMacros = macroses.get(name);
        return wrappedMacros.substring(1, wrappedMacros.length() - 1);
    }

    public int getMacrosAsInt(final String name) {
        return Integer.parseInt(macroses.get(name));
    }

    public double getMacrosAsDouble(final String name) {
        return Double.parseDouble(macroses.get(name));
    }

    public void addIncludedFile(final String filePath) {
        includedFiles.add(filePath);
    }

    public void addMapFile(final String filePath) {
        mapFiles.add(new MapFile(filePath));
    }

    public void addItem(final DmeItem item) {
        items.put(item.getType(), item);
    }

    public DmeItem getItemOrCreate(final String type) {
        DmeItem item = items.getOrDefault(type, new DmeItem(type, this));
        items.putIfAbsent(type, item);
        return item;
    }

    public DmeItem getItem(final String type) {
        return items.get(type);
    }

    public Map<String, String> getGlobalVars() {
        return items.get(ByondTypes.GLOBAL).getVars();
    }
}
