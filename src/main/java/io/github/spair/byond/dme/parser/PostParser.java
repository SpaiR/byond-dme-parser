package io.github.spair.byond.dme.parser;

import com.udojava.evalex.Expression;
import io.github.spair.byond.ByondTypes;
import io.github.spair.byond.dme.Dme;
import io.github.spair.byond.dme.DmeItem;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

final class PostParser {

    private final Dme dme;
    private final Map<String, String> globalVars;

    private final Map<String, DmeItem> additionalCreatedItems = new HashMap<>();
    private final List<DmeItem> roots = new ArrayList<>();
    private final Set<String> itemsWithLookedVars = new HashSet<>();

    private final Pattern letterPattern = Pattern.compile("[a-zA-Z]+");
    private final String[] mathSymbols = {"+", "-", "*", "/"};

    private PostParser(final Dme dme) {
        this.dme = dme;
        this.globalVars = dme.getGlobalVars();
    }

    static void parse(final Dme dme) {
        new PostParser(dme).doParse();
    }

    private void doParse() {
        dme.getItems().forEach((type, item) -> {
            if (notGlobalObject(type)) {
                assignParent(item);
                replaceGlobalVarsInItemWithValues(item);
                evaluateMathExpressionIfExist(item);
                addToRootsIfAble(item);
            }
        });

        addAdditionalItemsToDme();

        executeAsync(
                CompletableFuture.runAsync(this::assignAllSubtypesFromRoots),
                CompletableFuture.runAsync(this::lookupAllParentsVars)
        );
    }

    private void assignParent(final DmeItem item) {
        if (hasParent(item.getType())) {
            connectParentAndChild(determineParent(item.getType()), item);
        }
    }

    // Makes parents to know about every existed subtype.
    private void assignAllSubtypesFromRoots() {
        roots.forEach(this::setAndReturnAllSubtypes);
    }

    private Set<String> setAndReturnAllSubtypes(final DmeItem item) {
        Set<String> tempSubtypes = new HashSet<>();
        Set<String> itemSubtypes = item.getSubtypes();

        itemSubtypes.forEach(subtype -> tempSubtypes.addAll(setAndReturnAllSubtypes(dme.getItem(subtype))));
        itemSubtypes.addAll(tempSubtypes);

        return itemSubtypes;
    }

    private void lookupAllParentsVars() {
        dme.getItems().forEach((type, item) -> lookUpVars(item, dme.getItem(item.getParentPath())));
    }

    private void lookUpVars(final DmeItem item, final DmeItem parent) {
        if (parent != null) {
            if (!itemsWithLookedVars.contains(parent.getType()) && hasParent(parent.getType())) {
                lookUpVars(parent, dme.getItem(parent.getParentPath()));
            }
            parent.getVars().forEach(item.getVars()::putIfAbsent);
            itemsWithLookedVars.add(item.getType());
        }
    }

    private void replaceGlobalVarsInItemWithValues(final DmeItem item) {
        item.getVars().forEach((name, value) -> item.setVar(name, WordDefineChecker.check(value, globalVars)));
    }

    private void evaluateMathExpressionIfExist(final DmeItem item) {
        item.getVars().forEach((name, value) -> {
            if (noLetterMarkers(value) && hasMathMarkers(value)) {
                double newValue = new Expression(value).eval().doubleValue();
                item.setVar(name, newValue);
            }
        });
    }

    private boolean noLetterMarkers(final String text) {
        return !text.contains("\"") && !text.contains("'") && !letterPattern.matcher(text).find();
    }

    private boolean hasMathMarkers(final String text) {
        for (String mathSymbol : mathSymbols) {
            if (text.contains(mathSymbol)) {
                return true;
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

    private boolean notGlobalObject(final String type) {
        return !ByondTypes.GLOBAL.equals(type);
    }

    // During parent determining additional items are created. They are not declared in project,
    // and exist in form of intermediate objects, but they should exist in Dme.
    private void addAdditionalItemsToDme() {
        additionalCreatedItems.values().forEach(dme::addItem);
    }

    private void executeAsync(final CompletableFuture... futures) {
        try {
            CompletableFuture.allOf(futures).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
