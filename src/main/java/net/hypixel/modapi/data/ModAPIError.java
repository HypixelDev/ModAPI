package net.hypixel.modapi.data;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ModAPIError {
    DISABLED(0),
    INTERNAL_SERVER_ERROR(1),
    RATE_LIMITED(2),
    INVALID_PACKET_VERSION(3),
    NO_LONGER_SUPPORTED(4),
    ;

    private static final Map<Byte, ModAPIError> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(ModAPIError::getId, Function.identity()));

    public static ModAPIError getById(byte id) {
        return BY_ID.get(id);
    }

    private final byte id;

    ModAPIError(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
