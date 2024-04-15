package net.hypixel.modapi.packet.handler;

import net.hypixel.data.region.Environment;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.PacketHandler;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundLocationPacket;

public class LocationHandler implements PacketHandler<ServerboundLocationPacket> {
    @Override
    public ClientboundHypixelPacket handle(String identifier, ServerboundLocationPacket packet) {
        return new ClientboundLocationPacket(
                Environment.TEST,
                "chi-hp-bungee1",
                "mini1A",
                GameType.HOUSING,
                null,
                null,
                null
        );
    }
}
