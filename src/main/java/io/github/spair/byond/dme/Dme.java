package io.github.spair.byond.dme;

import lombok.Data;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

@Data
@SuppressWarnings("unused")
public class Dme {

    private Map<String, String> macroses = new HashMap<>();
    private List<String> includedFiles = new ArrayList<>();
    private List<String> mapFiles = new ArrayList<>();
    private Map<String, DmeItem> items = new TreeMap<>();

    public void addMacros(final String name, final String value) {
        macroses.put(name, value);
    }

    public String getMacros(final String name) {
        return macroses.get(name);
    }

    public void addIncludedFile(final String filePath) {
        includedFiles.add(filePath);
    }

    public void addMapFile(final String filePath) {
        mapFiles.add(filePath);
    }

    public void addItem(final DmeItem item) {
        items.put(item.getType(), item);
    }

    public DmeItem getItemOrCreate(final String type) {
        DmeItem item = items.getOrDefault(type, new DmeItem(type));
        items.putIfAbsent(type, item);
        return item;
    }

    public DmeItem getItem(final String type) {
        return items.get(type);
    }

    public DmeItem getGlobalObject() {
        return items.get(ByondTypes.GLOBAL);
    }
}
