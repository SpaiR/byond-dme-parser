package io.github.spair.byond.dme;

import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.VarWrapper;
import lombok.Data;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
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

    public void mergeWithJson(final File jsonFile) {
        try {
            mergeWithJson(new String(Files.readAllBytes(jsonFile.toPath()), Charset.defaultCharset()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void mergeWithJson(final String json) {
        DmeJsonMerger.merge(json, this);
    }

    public void mergeWithJson(final InputStream is) {
        val bis = new BufferedInputStream(is);
        try (val buf = new ByteArrayOutputStream()) {
            int result;
            while ((result = bis.read()) != -1) {
                byte b = (byte) result;
                buf.write(b);
            }
            mergeWithJson(buf.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    ///////////////// Macros

    public void addMacros(final String name, final String value) {
        macroses.put(name, value);
    }

    public void addMacros(final String name, final Number value) {
        macroses.put(name, String.valueOf(value));
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

    public String getMacrosText(final String name) {
        return VarWrapper.optionalText(macroses.get(name)).get();
    }

    public Optional<String> getMacrosTextSafe(final String name) {
        return VarWrapper.optionalText(macroses.get(name));
    }

    public String getMacrosFilePath(final String name) {
        return VarWrapper.optionalFilePath(macroses.get(name)).get();
    }

    public Optional<String> getMacrosFilePathSafe(final String name) {
        return VarWrapper.optionalFilePath(macroses.get(name));
    }

    public Integer getMacrosInt(final String name) {
        return VarWrapper.optionalInt(macroses.get(name)).get();
    }

    public Optional<Integer> getMacrosIntSafe(final String name) {
        return VarWrapper.optionalInt(macroses.get(name));
    }

    public Double getMacrosDouble(final String name) {
        return VarWrapper.optionalDouble(macroses.get(name)).get();
    }

    public Optional<Double> getMacrosDoubleSafe(final String name) {
        return VarWrapper.optionalDouble(macroses.get(name));
    }

    public Boolean getMacrosBool(final String name) {
        return getMacrosBoolSafe(name).get();
    }

    public Optional<Boolean> getMacrosBoolSafe(final String name) {
        return VarWrapper.optionalBoolean(macroses.get(name));
    }

    ///////////////// Included / Map files

    public void addIncludedFile(final String filePath) {
        includedFiles.add(filePath);
    }

    public void addMapFile(final String filePath) {
        mapFiles.add(filePath);
    }

    ///////////////// Item

    public void addItem(final DmeItem item) {
        items.put(item.getType(), item);
    }

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

    public String getGlobalVarText(final String name) {
        return VarWrapper.optionalText(getGlobalVars().get(name)).get();
    }

    public Optional<String> getGlobalVarTextSafe(final String name) {
        return VarWrapper.optionalText(getGlobalVars().get(name));
    }

    public String getGlobalFilePath(final String name) {
        return VarWrapper.optionalFilePath(getGlobalVars().get(name)).get();
    }

    public Optional<String> getGlobalFilePathSafe(final String name) {
        return VarWrapper.optionalFilePath(getGlobalVars().get(name));
    }

    public Integer getGlobalVarInt(final String name) {
        return VarWrapper.optionalInt(getGlobalVars().get(name)).get();
    }

    public Optional<Integer> getGlobalVarIntSafe(final String name) {
        return VarWrapper.optionalInt(getGlobalVars().get(name));
    }

    public Double getGlobalVarDouble(final String name) {
        return VarWrapper.optionalDouble(getGlobalVars().get(name)).get();
    }

    public Optional<Double> getGlobalVarDoubleSafe(final String name) {
        return VarWrapper.optionalDouble(getGlobalVars().get(name));
    }

    public Boolean getGlobalVarBool(final String name) {
        return getGlobalVarBoolSafe(name).get();
    }

    public Optional<Boolean> getGlobalVarBoolSafe(final String name) {
        return VarWrapper.optionalBoolean(getGlobalVars().get(name));
    }
}
