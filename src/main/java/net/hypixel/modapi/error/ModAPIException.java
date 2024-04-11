package net.hypixel.modapi.error;

import net.hypixel.modapi.packet.HypixelPacketType;

public class ModAPIException extends RuntimeException {
    private final HypixelPacketType packetType;
    private final ErrorReason reason;

    public ModAPIException(HypixelPacketType packetType, ErrorReason reason) {
        super(String.format("Received error response '%s' from packet '%s'", reason, packetType));
        this.packetType = packetType;
        this.reason = reason;
    }

    public HypixelPacketType getPacketType() {
        return packetType;
    }

    public ErrorReason getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ModAPIException{" +
                "packetType=" + packetType +
                ", reason=" + reason +
                "} " + super.toString();
    }
}
