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

public class HypixelModAPI {
    private static final HypixelModAPI INSTANCE = new HypixelModAPI();

    public static HypixelModAPI getInstance() {
        return INSTANCE;
    }

    private final PacketRegistry packetRegistry = new PacketRegistry();
    private final List<ClientboundPacketHandler> packetHandlers = new CopyOnWriteArrayList<>();

    private HypixelModAPI() {
        packetRegistry.registerPacketType("hypixel:ping", ClientboundPingPacket::new);
        packetRegistry.registerPacketType("hypixel:location", ClientboundLocationPacket::new);
        packetRegistry.registerPacketType("hypixel:party_info", ClientboundPartyInfoPacket::new);
        packetRegistry.registerPacketType("hypixel:player_info", ClientboundPlayerInfoPacket::new);
    }

    public PacketRegistry getPacketRegistry() {
        return packetRegistry;
    }

    public void registerHandler(ClientboundPacketHandler handler) {
        packetHandlers.add(handler);
    }

    public void handle(String identifier, PacketSerializer serializer) {
        if (packetHandlers.isEmpty()) {
            return;
        }

        // All responses contain a boolean of if the response is a success, if not then a further var int is included to identify the error
        if (!serializer.readBoolean()) {
            ErrorReason reason = ErrorReason.getById(serializer.readVarInt());
            throw new ModAPIException(identifier, reason);
        }

        HypixelPacket packet = packetRegistry.createPacket(identifier, serializer);
        for (ClientboundPacketHandler handler : packetHandlers) {
            handler.handle(packet);
        }
    }

}
