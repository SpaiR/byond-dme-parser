package io.github.spair.byond.dme.parser;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

final class DmeInitializer {

    private static final String INITIAL_DME_FILE = "initial_dme.json";

    private static final String MACROSES = "macroses";
    private static final String DEFINITIONS = "definitions";

    private static final String TYPE = "type";
    private static final String PARENT = "parent";
    private static final String SUBTYPES = "subtypes";
    private static final String VARS = "vars";
    private static final String LIST = "list(";

    private final Dme dme;

    private DmeInitializer(final Dme dme) {
        this.dme = dme;
    }

    static Dme initialize(final Dme dme) {
        return new DmeInitializer(dme).doInit();
    }

    private Dme doInit() {
        final JsonObject initDmeJson = parseInitialDmeJson();

        initDmeJson.get(MACROSES).asObject().forEach(this::addMacros);
        initDmeJson.get(DEFINITIONS).asArray().forEach(this::addDefinition);

        return dme;
    }

    private JsonObject parseInitialDmeJson() {
        try (Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(INITIAL_DME_FILE))) {
            return Json.parse(reader).asObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void addMacros(final JsonObject.Member macros) {
        final String name = macros.getName();
        final JsonValue value = macros.getValue();

        if (value.isNumber()) {
            dme.addMacros(name, Integer.toString(value.asInt()));
        } else {
            dme.addMacros(name, '"' + value.asString() + '"');
        }
    }

    private void addDefinition(final JsonValue defEntry) {
        final JsonObject object = defEntry.asObject();

        final String type = object.get(TYPE).asString();
        final String parent = object.get(PARENT).asString();
        final JsonArray subtypes = object.get(SUBTYPES).asArray();
        final JsonObject vars = object.get(VARS).asObject();

        DmeItem item = new DmeItem(type, dme);

        item.setParentPath(parent);
        subtypes.forEach(subtypeValue -> item.addSubtype(subtypeValue.asString()));
        vars.forEach(var -> {
            final String name = var.getName();
            final JsonValue value = var.getValue();

            if (value.isNull()) {
                item.setEmptyVar(name);
            } else if (value.isNumber()) {
                item.setVar(name, value.asInt());
            } else if (value.isString()) {
                final String stringValue = value.asString();

                if (stringValue.startsWith(LIST)) {
                    item.setVar(name, stringValue);
                } else {
                    item.setQuotedVar(name, stringValue);
                }
            } else {
                throw new IllegalArgumentException(
                        "Unknown type of variable found in JSON. Name: " + name + ", value: " + value
                );
            }
        });

        dme.addItem(item);
    }
}
