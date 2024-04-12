package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class PacketRegistry {

    private final Map<String, RegisteredType> packetRegistry = new ConcurrentHashMap<>();

    public void registerPacketType(String identifier,
                                   Function<PacketSerializer, HypixelPacket> clientPacketFactory,
                                   Function<PacketSerializer, HypixelPacket> serverPacketFactory) {
        packetRegistry.put(identifier, new RegisteredType(clientPacketFactory, serverPacketFactory));
    }

    private RegisteredType getRegisteredType(String identifier) {
        RegisteredType registeredType = packetRegistry.get(identifier);
        if (registeredType == null) {
            throw new IllegalArgumentException("Unknown packet identifier: " + identifier);
        }
        return registeredType;
    }

    public HypixelPacket createClientboundPacket(String identifier, PacketSerializer serializer) {
        return getRegisteredType(identifier).clientPacketFactory.apply(serializer);
    }

    public HypixelPacket createServerboundPacket(String identifier, PacketSerializer serializer) {
        return getRegisteredType(identifier).serverPacketFactory.apply(serializer);
    }

    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(packetRegistry.keySet());
    }

    private static final class RegisteredType {

        private final Function<PacketSerializer, HypixelPacket> clientPacketFactory;
        private final Function<PacketSerializer, HypixelPacket> serverPacketFactory;

        public RegisteredType(Function<PacketSerializer, HypixelPacket> clientPacketFactory,
                              Function<PacketSerializer, HypixelPacket> serverPacketFactory) {
            this.clientPacketFactory = clientPacketFactory;
            this.serverPacketFactory = serverPacketFactory;
        }
    }

}
