package net.hypixel.modapi.handler;

import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;

@FunctionalInterface
public interface ErrorHandler<T extends ClientboundHypixelPacket> {
    void onError(ErrorReason reason);
}
