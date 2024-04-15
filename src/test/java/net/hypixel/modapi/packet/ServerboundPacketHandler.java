package net.hypixel.modapi.packet;

import net.hypixel.modapi.packet.handler.LocationHandler;
import net.hypixel.modapi.packet.handler.PartyInfoHandler;
import net.hypixel.modapi.packet.handler.PingHandler;
import net.hypixel.modapi.packet.handler.PlayerInfoHandler;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundLocationPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPlayerInfoPacket;

import java.util.HashMap;
import java.util.Map;

class ServerboundPacketHandler {
    private final Map<Class<? extends HypixelPacket>, PacketHandler> packetHandlers = new HashMap<>();

    ServerboundPacketHandler() {
        register(ServerboundPingPacket.class, new PingHandler());
        register(ServerboundLocationPacket.class, new LocationHandler());
        register(ServerboundPartyInfoPacket.class, new PartyInfoHandler());
        register(ServerboundPlayerInfoPacket.class, new PlayerInfoHandler());
    }

    private <T extends HypixelPacket> void register(Class<T> packetClass, PacketHandler<T> handler) {
        packetHandlers.put(packetClass, handler);
    }

    public ClientboundHypixelPacket handle(String identifier, HypixelPacket packet) {
        PacketHandler handler = packetHandlers.get(packet.getClass());
        if (handler == null) {
            return null;
        }

        return handler.handle(identifier, packet);
    }

}
