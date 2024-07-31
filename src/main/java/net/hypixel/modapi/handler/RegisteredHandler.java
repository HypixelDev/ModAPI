package net.hypixel.modapi.handler;

import net.hypixel.modapi.packet.ClientboundHypixelPacket;

public interface RegisteredHandler<T extends ClientboundHypixelPacket> {
    /**
     * Handling for when an error is received for this registered handler.
     * <br>
     * Note: This error may be received due to any modification requesting the same packet type.
     */
    void onError(ErrorHandler<T> errorHandler);
}
