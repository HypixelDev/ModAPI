package net.hypixel.modapi;

import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.error.ModAPIException;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.PacketRegistry;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class HypixelModAPI {
    private static final HypixelModAPI INSTANCE = new HypixelModAPI();

    public static HypixelModAPI getInstance() {
        return INSTANCE;
    }

    private final PacketRegistry registry = new PacketRegistry();
    private final List<ClientboundPacketHandler> handlers = new CopyOnWriteArrayList<>();

    private HypixelModAPI() {
        registry.registerPacketType("hypixel:ping", ClientboundPingPacket::new);
        registry.registerPacketType("hypixel:location", ClientboundLocationPacket::new);
        registry.registerPacketType("hypixel:party_info", ClientboundPartyInfoPacket::new);
        registry.registerPacketType("hypixel:player_info", ClientboundPlayerInfoPacket::new);
    }

    public PacketRegistry getRegistry() {
        return registry;
    }

    public void registerHandler(ClientboundPacketHandler handler) {
        handlers.add(handler);
    }

    public void handle(String identifier, PacketSerializer serializer) {
        if (handlers.isEmpty()) {
            return;
        }

        Function<PacketSerializer, HypixelPacket> factory = registry.getPacketFactory(identifier);
        if (factory == null) {
            return;
        }

        // All responses contain a boolean of if the response is a success, if not then a further var int is included to identify the error
        if (!serializer.readBoolean()) {
            ErrorReason reason = ErrorReason.getById(serializer.readVarInt());
            throw new ModAPIException(identifier, reason);
        }

        HypixelPacket packet = factory.apply(serializer);
        for (ClientboundPacketHandler handler : handlers) {
            handler.handle(packet);
        }
    }

}
