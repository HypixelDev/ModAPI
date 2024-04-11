package net.hypixel.modapi.error;

public interface ErrorReason {
    int getId();

    static ErrorReason getById(int id) {
        BuiltinErrorReason reason = BuiltinErrorReason.getById(id);
        if (reason != null) {
            return reason;
        }

        return new UnknownErrorReason(id);
    }
}
