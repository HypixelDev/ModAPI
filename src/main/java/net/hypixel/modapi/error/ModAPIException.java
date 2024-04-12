package net.hypixel.modapi.error;

public class ModAPIException extends RuntimeException {
    private final String identifier;
    private final ErrorReason reason;

    public ModAPIException(String identifier, ErrorReason reason) {
        super(String.format("Received error response '%s' from packet '%s'", reason, identifier));
        this.identifier = identifier;
        this.reason = reason;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ErrorReason getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ModAPIException{" +
                "identifier='" + identifier + '\'' +
                ", reason=" + reason +
                "} " + super.toString();
    }
}
