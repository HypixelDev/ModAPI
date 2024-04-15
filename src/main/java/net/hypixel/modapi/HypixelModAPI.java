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
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class HypixelModAPI {
    private static final HypixelModAPI INSTANCE = new HypixelModAPI();

    public static HypixelModAPI getInstance() {
        return INSTANCE;
    }

    private final PacketRegistry registry = new PacketRegistry();
    private final List<ClientboundPacketHandler> handlers = new CopyOnWriteArrayList<>();

    private HypixelModAPI() {
        registry.define("hypixel:ping")
                .clientbound(ClientboundPingPacket.class, ClientboundPingPacket::new)
                .serverbound(ServerboundPingPacket.class, ServerboundPingPacket::new)
                .register();
        registry.define("hypixel:location")
                .clientbound(ClientboundLocationPacket.class, ClientboundLocationPacket::new)
                .serverbound(ServerboundLocationPacket.class, ServerboundLocationPacket::new)
                .register();
        registry.define("hypixel:party_info")
                .clientbound(ClientboundPartyInfoPacket.class, ClientboundPartyInfoPacket::new)
                .serverbound(ServerboundPartyInfoPacket.class, ServerboundPartyInfoPacket::new)
                .register();
        registry.define("hypixel:player_info")
                .clientbound(ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacket::new)
                .serverbound(ServerboundPlayerInfoPacket.class, ServerboundPlayerInfoPacket::new)
                .register();
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

        if (!registry.isRegistered(identifier)) {
            return;
        }

        // All responses contain a boolean of if the response is a success, if not then a further var int is included to identify the error
        Optional<ErrorReason> errorReasonOptional = serializer.readOptional(PacketSerializer::readVarInt).map(ErrorReason::getById);
        if (errorReasonOptional.isPresent()) {
            throw new ModAPIException(identifier, errorReasonOptional.get());
        }

        ClientboundHypixelPacket packet = registry.createClientboundPacket(identifier, serializer);
        for (ClientboundPacketHandler handler : handlers) {
            packet.handle(handler);
        }
    }

}
