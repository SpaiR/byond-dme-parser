package io.github.spair.byond.dme;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

final class DmeInitializer {

    private static final String INITIAL_DME_FILE = "initial_dme.json";
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
        DmeInitializer initializer = new DmeInitializer(dme);
        initializer.doInit();
        return dme;
    }

    private void doInit() {
        parseInitialDmeJson().asArray().forEach(jsonValue -> {
            final JsonObject object = jsonValue.asObject();

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
                    item.setNumberVar(name, value.asInt());
                } else if (value.isString()) {
                    final String stringValue = value.asString();

                    if (stringValue.startsWith(LIST)) {
                        item.setVar(name, stringValue);
                    } else {
                        item.setStringVar(name, stringValue);
                    }
                } else {
                    throw new IllegalArgumentException("Unknown type of variable found in JSON. Name: " + name);
                }
            });

            dme.addItem(item);
        });
    }

    private JsonValue parseInitialDmeJson() {
        try (Reader reader = new FileReader(ResourceUtil.loadFile(INITIAL_DME_FILE))) {
            return Json.parse(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
