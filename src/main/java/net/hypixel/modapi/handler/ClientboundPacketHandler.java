package net.hypixel.modapi.handler;

import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;

@FunctionalInterface
public interface ClientboundPacketHandler<T extends ClientboundHypixelPacket> {
    void handle(T packet);

    /**
     * Optional handling for when an error is received for this packet.
     * <br>
     * Note: This error may be received due to any modification requesting this packet.
     */
    default void onError(ErrorReason reason) {}
}
