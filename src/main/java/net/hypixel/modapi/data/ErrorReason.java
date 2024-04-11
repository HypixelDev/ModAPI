package net.hypixel.modapi.data;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ErrorReason {
    UNKNOWN(0),
    DISABLED(1),
    INTERNAL_SERVER_ERROR(2),
    RATE_LIMITED(3),
    INVALID_PACKET_VERSION(4),
    NO_LONGER_SUPPORTED(5),
    ;

    private static final Map<Integer, ErrorReason> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(ErrorReason::getId, Function.identity()));

    public static ErrorReason getById(int id) {
        ErrorReason reason = BY_ID.get(id);
        if (reason != null) {
            return reason;
        }
        return UNKNOWN;
    }

    private final int id;

    ErrorReason(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
