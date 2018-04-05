package io.github.spair.byond.dme;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class PostParser {

    private final Dme dme;
    private final Map<String, String> globalVars;

    private final Map<String, DmeItem> additionalCreatedItems = new HashMap<>();
    private final List<DmeItem> roots = new ArrayList<>();
    private final Set<String> itemsWithLookedVars = new HashSet<>();

    PostParser(final Dme dme) {
        this.dme = dme;
        this.globalVars = dme.getGlobalObject().getVars();
    }

    void parse() {
        dme.getItems().forEach((type, item) -> {
            if (notGlobalObject(type)) {
                assignParent(item);
                replaceGlobalVarsInItemWithValues(item);
                addToRootsIfAble(item);
            }
        });

        addAdditionalItemsToDme();
        executeConcurrentTasks();
    }

    private void executeConcurrentTasks() {
        List<Callable<Void>> taskList = Arrays.asList(this::assignAllSubtypesFromRoots, this::lookupAllParentsVars);
        ExecutorService executor = Executors.newWorkStealingPool(taskList.size());

        try {
            executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    private void assignParent(final DmeItem item) {
        if (hasParent(item.getType())) {
            connectParentAndChild(determineParent(item.getType()), item);
        }
    }

    // Makes parents to know about every existed subtype.
    private Void assignAllSubtypesFromRoots() {
        roots.forEach(this::setAndReturnAllSubtypes);
        return null;
    }

    private Set<String> setAndReturnAllSubtypes(final DmeItem item) {
        Set<String> tempSubtypes = new HashSet<>();
        Set<String> itemSubtypes = item.getSubtypes();

        itemSubtypes.forEach(subtype -> tempSubtypes.addAll(setAndReturnAllSubtypes(dme.getItem(subtype))));
        itemSubtypes.addAll(tempSubtypes);

        return itemSubtypes;
    }

    private Void lookupAllParentsVars() {
        dme.getItems().forEach((type, item) -> lookUpVars(item, dme.getItem(item.getParentPath())));
        return null;
    }

    private void lookUpVars(final DmeItem item, final DmeItem parent) {
        if (Objects.nonNull(parent)) {
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

    private DmeItem determineParent(final String type) {
        String parentPath = ByondTypes.DATUM;

        if (type.indexOf('/') != type.lastIndexOf('/')) {
            parentPath = type.substring(0, type.lastIndexOf('/'));
        }

        DmeItem parent = dme.getItem(parentPath);

        if (Objects.isNull(parent)) {
            if (additionalCreatedItems.containsKey(parentPath)) {
                parent = additionalCreatedItems.get(parentPath);
            } else {
                parent = new DmeItem(parentPath);
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

    private void addAdditionalItemsToDme() {
        additionalCreatedItems.values().forEach(dme::addItem);
    }
}
