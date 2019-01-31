package io.github.spair.byond.dme;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import lombok.val;
import lombok.var;

final class DmeJsonMerger {

    private static final String TEXT_TYPE = "{text}";

    private static final String MACROS_KEY = "macroses";
    private static final String DEF_KEY = "definitions";

    private static final String TYPE_PROP = "type";
    private static final String PARENT_PROP = "parent";
    private static final String SUBTYPES_PROP = "subtypes";
    private static final String VARS_PROP = "vars";

    static void merge(final String json, final Dme dme) {
        val jsonObject = Json.parse(json).asObject();

        var subPart = jsonObject.get(MACROS_KEY);
        if (subPart != null) {
            addMacrossesToDme(subPart.asObject(), dme);
        }

        subPart = jsonObject.get(DEF_KEY);
        if (subPart != null) {
            addDefinitionsToDme(subPart.asArray(), dme);
        }
    }

    private static void addMacrossesToDme(final JsonObject macroses, final Dme dme) {
        for (val member : macroses) {
            val name = member.getName();
            val value = member.getValue();
            if (value.isNumber()) {
                dme.addMacros(name, getNumberFromValue(value));
            } else {
                val valueString = value.asString();
                if (valueString.startsWith(TEXT_TYPE)) {
                    dme.addMacrosText(name, valueString.substring(TEXT_TYPE.length()));
                } else {
                    dme.addMacros(name, valueString);
                }
            }
        }
    }

    private static void addDefinitionsToDme(final JsonArray definitions, final Dme dme) {
        for (val definition : definitions) {
            val defObject = definition.asObject();

            val type = defObject.get(TYPE_PROP).asString();
            val parent = defObject.get(PARENT_PROP);
            val subtypes = defObject.get(SUBTYPES_PROP);
            val vars = defObject.get(VARS_PROP);

            val item = dme.getItemOrCreate(type);

            if (parent != null) {
                item.setParentPath(parent.asString());
            }

            if (subtypes != null) {
                for (val subtype : subtypes.asArray()) {
                    item.addSubtype(subtype.asString());
                }
            }

            if (vars != null) {
                for (val var : vars.asObject()) {
                    val name = var.getName();
                    val value = var.getValue();

                    if (value.isNull()) {
                        item.setEmptyVar(name);
                    } else if (value.isNumber()) {
                        item.setVar(name, getNumberFromValue(value));
                    } else if (value.isString()) {
                        val valueString = value.asString();
                        if (valueString.startsWith(TEXT_TYPE)) {
                            item.setVarText(name, valueString.substring(TEXT_TYPE.length()));
                        } else {
                            item.setVar(name, valueString);
                        }
                    } else if (value.isBoolean()) {
                        item.setVar(name, String.valueOf(value.asBoolean()));
                    } else {
                        throw new IllegalArgumentException("Unknown type of variable found in JSON. Name: " + name + ", value: " + value);
                    }
                }
            }
        }
    }

    private static Number getNumberFromValue(final JsonValue value) {
        val doubleNum = value.asDouble();
        val longNum = (long) doubleNum;
        if (longNum == doubleNum) {
            return longNum;
        } else {
            return doubleNum;
        }
    }

    private DmeJsonMerger() {
    }
}
