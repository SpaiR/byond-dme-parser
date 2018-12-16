package io.github.spair.byond;

/**
 * Constants with general BYOND types.
 */
@SuppressWarnings("unused")
public final class ByondTypes {

    public static final String NULL = "null";

    /** Holder to store all variables with global scope. */
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
