package net.hypixel.modapi.error;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BuiltinErrorReason implements ErrorReason {
    DISABLED(1),
    INTERNAL_SERVER_ERROR(2),
    RATE_LIMITED(3),
    INVALID_PACKET_VERSION(4),
    NO_LONGER_SUPPORTED(5),
    ;

    private static final Map<Integer, BuiltinErrorReason> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(BuiltinErrorReason::getId, Function.identity()));

    static BuiltinErrorReason getById(int id) {
        return BY_ID.get(id);
    }

    private final int id;

    BuiltinErrorReason(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
