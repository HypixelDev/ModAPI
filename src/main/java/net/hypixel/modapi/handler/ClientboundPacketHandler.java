package net.hypixel.modapi.handler;

import net.hypixel.modapi.packet.ClientboundHypixelPacket;

@FunctionalInterface
public interface ClientboundPacketHandler<T extends ClientboundHypixelPacket> {
    void handle(T packet);
}
