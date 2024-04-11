package net.hypixel.modapi.data;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ErrorReason {
    DISABLED(0),
    INTERNAL_SERVER_ERROR(1),
    RATE_LIMITED(2),
    INVALID_PACKET_VERSION(3),
    NO_LONGER_SUPPORTED(4),
    ;

    private static final Map<Byte, ErrorReason> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(ErrorReason::getId, Function.identity()));

    public static ErrorReason getById(byte id) {
        return BY_ID.get(id);
    }

    private final byte id;

    ErrorReason(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
