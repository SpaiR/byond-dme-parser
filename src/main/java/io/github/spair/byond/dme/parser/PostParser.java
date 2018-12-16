package io.github.spair.byond.dme.parser;

import com.udojava.evalex.Expression;
import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;
import lombok.val;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;

final class PostParser {

    private final Dme dme;
    private final Map<String, String> globalVars;

    private final Map<String, DmeItem> additionalCreatedItems = new HashMap<>();
    private final List<DmeItem> roots = new ArrayList<>();

    private final Pattern letterPattern = Pattern.compile("[a-zA-Zа-яА-Я]+");
    private final Pattern numberPattern = Pattern.compile("\\d+");

    private final String[] mathSymbols = {"+", "-", "*", "/"};

    PostParser(final Dme dme) {
        this.dme = dme;
        this.globalVars = dme.getGlobalVars();
    }

    void doParse() {
        for (val itemEntry : dme.getItems().entrySet()) {
            val item = itemEntry.getValue();
            if (!ByondTypes.GLOBAL.equals(itemEntry.getKey())) {
                assignParent(item);
                replaceGlobalVarsInItemWithValues(item);
                evaluateMathExpressionIfExist(item);
                addToRootsIfAble(item);
            }
        }
        addAdditionalItemsToDme();
        assignAllSubtypesFromRoots();
    }

    private void assignParent(final DmeItem item) {
        if (hasParent(item.getType())) {
            connectParentAndChild(determineParent(item.getType()), item);
        }
    }

    // Makes parents to know about every existed subtype.
    private void assignAllSubtypesFromRoots() {
        for (val root : roots) {
            setAndReturnAllSubtypes(root);
        }
    }

    private Set<String> setAndReturnAllSubtypes(final DmeItem item) {
        Set<String> tempSubtypes = new HashSet<>();
        Set<String> itemSubtypes = item.getSubtypes();

        for (val subtype : itemSubtypes) {
            tempSubtypes.addAll(setAndReturnAllSubtypes(dme.getItem(subtype)));
        }
        itemSubtypes.addAll(tempSubtypes);

        return itemSubtypes;
    }

    private void replaceGlobalVarsInItemWithValues(final DmeItem item) {
        for (val varEntry : item.getVars().entrySet()) {
            item.setVar(varEntry.getKey(), WordDefineChecker.check(varEntry.getValue(), globalVars));
        }
    }

    private void evaluateMathExpressionIfExist(final DmeItem item) {
        for (val varEntry : item.getVars().entrySet()) {
            val value = varEntry.getValue();
            if (noLetterMarkers(value) && hasMathMarkers(value)) {
                try {
                    double newValue = new Expression(value).eval().doubleValue();
                    item.setVar(varEntry.getKey(), newValue);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private boolean noLetterMarkers(final String text) {
        return !text.contains("\"") && !text.contains("'") && !letterPattern.matcher(text).find();
    }

    private boolean hasMathMarkers(final String text) {
        for (val mathSymbol : mathSymbols) {
            if (text.contains(mathSymbol)) {
                val m = numberPattern.matcher(text);
                int matchCount = 0;
                while (m.find()) {
                    matchCount++;
                }
                return matchCount > 1;
            }
        }
        return false;
    }

    private DmeItem determineParent(final String type) {
        String parentPath = ByondTypes.DATUM;

        if (type.indexOf('/') != type.lastIndexOf('/')) {
            parentPath = type.substring(0, type.lastIndexOf('/'));
        }

        DmeItem parent = dme.getItem(parentPath);

        if (parent == null) {
            if (additionalCreatedItems.containsKey(parentPath)) {
                parent = additionalCreatedItems.get(parentPath);
            } else {
                parent = new DmeItem(parentPath, dme);
                connectParentAndChild(determineParent(parentPath), parent);
                additionalCreatedItems.put(parentPath, parent);
            }
        }

        return parent;
    }

    private void connectParentAndChild(final DmeItem parent, final DmeItem child) {
        parent.addSubtype(child.getType());
        if (child.getParentPath().isEmpty()) {
            child.setParentPath(parent.getType());
        }
    }

    private void addToRootsIfAble(final DmeItem item) {
        if (item.getParentPath().equals(ByondTypes.DATUM) || item.getType().equals(ByondTypes.DATUM)) {
            roots.add(item);
        }
    }

    private boolean hasParent(final String type) {
        return !(ByondTypes.DATUM.equals(type)
                || ByondTypes.CLIENT.equals(type)
                || ByondTypes.WORLD.equals(type)
                || ByondTypes.LIST.equals(type)
                || ByondTypes.SAVEFILE.equals(type)
        );
    }

    // During parent determining additional items are created. They are not declared in project,
    // and exist in form of intermediate objects, but they should exist in Dme.
    private void addAdditionalItemsToDme() {
        for (val dmeItem : additionalCreatedItems.values()) {
            dme.addItem(dmeItem);
        }
    }
}
