package io.github.spair.byond.dme;

/**
 * Constants with BYOND types.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ByondTypes {

    public static final String NULL = "null";
    /**
     * Not a BYOND type, but mostly a holder which is used by parser to store all variables with global scope.
     * It could be used in the next way: {@code Dme::getItem(ByondTypes.GLOBAL)}
     */
    public static final String GLOBAL = "GLOBAL";

    public static final String DATUM = "/datum";
    public static final String WORLD = "/world";
    public static final String CLIENT = "/client";
    public static final String LIST = "/list";
    public static final String SAVEFILE = "/savefile";

    public static final String ATOM = "/atom";
    public static final String ATOM_MOVABLE = "/atom/movable";

    public static final String AREA = "/area";
    public static final String TURF = "/turf";
    public static final String OBJ = "/obj";
    public static final String MOB = "/mob";

    private ByondTypes() {
    }
}
