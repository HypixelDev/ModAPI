package net.hypixel.modapi;

import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.error.ModAPIException;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.PacketRegistry;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundLocationPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPlayerInfoPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HypixelModAPI {
    private static final HypixelModAPI INSTANCE = new HypixelModAPI();

    public static HypixelModAPI getInstance() {
        return INSTANCE;
    }

    private final PacketRegistry registry = new PacketRegistry();
    private final List<ClientboundPacketHandler> handlers = new CopyOnWriteArrayList<>();

    private HypixelModAPI() {
        registry.registerPacketType("hypixel:ping",
                ClientboundPingPacket.class, ClientboundPingPacket::new,
                ServerboundPingPacket.class, ServerboundPingPacket::new);
        registry.registerPacketType("hypixel:location",
                ClientboundLocationPacket.class, ClientboundLocationPacket::new,
                ServerboundLocationPacket.class, ServerboundLocationPacket::new);
        registry.registerPacketType("hypixel:party_info",
                ClientboundPartyInfoPacket.class, ClientboundPartyInfoPacket::new,
                ServerboundPartyInfoPacket.class, ServerboundPartyInfoPacket::new);
        registry.registerPacketType("hypixel:player_info",
                ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacket::new,
                ServerboundPlayerInfoPacket.class, ServerboundPlayerInfoPacket::new);
    }

    public PacketRegistry getRegistry() {
        return registry;
    }

    public void registerHandler(ClientboundPacketHandler handler) {
        handlers.add(handler);
    }

    public void unregisterHandler(ClientboundPacketHandler handler) {
        handler.close();
        packetHandlers.remove(handler);
    }

    public void handle(String identifier, PacketSerializer serializer) {
        if (handlers.isEmpty()) {
            return;
        }

        if (!registry.isRegistered(identifier)) {
            return;
        }

        // All responses contain a boolean of if the response is a success, if not then a further var int is included to identify the error
        if (!serializer.readBoolean()) {
            ErrorReason reason = ErrorReason.getById(serializer.readVarInt());
            throw new ModAPIException(identifier, reason);
        }

        ClientboundHypixelPacket packet = registry.createClientboundPacket(identifier, serializer);
        for (ClientboundPacketHandler handler : handlers) {
            packet.handle(handler);
        }
    }

}
