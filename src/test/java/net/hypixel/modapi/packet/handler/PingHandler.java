package net.hypixel.modapi.packet.handler;

import net.hypixel.modapi.packet.PacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;

public class PingHandler implements PacketHandler<ServerboundPingPacket> {
    @Override
    public ClientboundHypixelPacket handle(String identifier, ServerboundPingPacket packet) {
        return new ClientboundPingPacket("pong");
    }
}
