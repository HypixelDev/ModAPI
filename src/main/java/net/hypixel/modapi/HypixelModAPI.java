package net.hypixel.modapi;

import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.error.ModAPIException;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.PacketRegistry;
import net.hypixel.modapi.packet.impl.clientbound.*;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundHelloEventPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationEventPacket;
import net.hypixel.modapi.packet.impl.serverbound.*;
import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class HypixelModAPI {
    private static final HypixelModAPI INSTANCE = new HypixelModAPI();

    public static HypixelModAPI getInstance() {
        return INSTANCE;
    }

    private final PacketRegistry registry = new PacketRegistry();
    private final List<ClientboundPacketHandler> handlers = new CopyOnWriteArrayList<>();
    private Consumer<HypixelPacket> packetSender = null;

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

        registry.define("hypixel:register")
                .serverbound(ServerboundRegisterPacket.class, ServerboundRegisterPacket::new)
                .register();

        registry.define("hyevent:hello")
                .clientbound(ClientboundHelloEventPacket.class, ClientboundHelloEventPacket::new)
                .register();
        registry.define("hyevent:location")
                .clientbound(ClientboundLocationEventPacket.class, ClientboundLocationEventPacket::new)
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
        if (!serializer.readBoolean()) {
            ErrorReason reason = ErrorReason.getById(serializer.readVarInt());
            throw new ModAPIException(identifier, reason);
        }

        ClientboundHypixelPacket packet = registry.createClientboundPacket(identifier, serializer);
        if (packet instanceof ClientboundVersionedPacket && !((ClientboundVersionedPacket) packet).isExpectedVersion()) {
            // Ignore packets that don't match our expected version, these could be received due to other mods requesting them
            return;
        }

        handle(packet);
    }

    public void handle(ClientboundHypixelPacket packet) {
        for (ClientboundPacketHandler handler : handlers) {
            packet.handle(handler);
        }
    }

    public void setPacketSender(Consumer<HypixelPacket> packetSender) {
        if (this.packetSender != null) {
            throw new IllegalArgumentException("Packet sender already set");
        }
        this.packetSender = packetSender;
    }

    public void sendPacket(HypixelPacket packet) {
        if (packetSender == null) {
            throw new IllegalStateException("Packet sender not set");
        }
        packetSender.accept(packet);
    }
}
