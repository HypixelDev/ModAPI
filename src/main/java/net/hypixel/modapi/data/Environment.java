package net.hypixel.modapi.data;

public enum Environment {
    PRODUCTION, // The Production Hypixel Network - "mc.hypixel.net"
    BETA, // The Alpha Hypixel Network - "alpha.hypixel.net"
    TEST, // The Test Hypixel Network - For Hypixel Developers
    ;

    public static final Environment[] VALUES = values();
}
