package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class PacketRegistry {

    private final Map<String, Function<PacketSerializer, HypixelPacket>> packetRegistry = new ConcurrentHashMap<>();

    public void registerPacketType(String identifier, Function<PacketSerializer, HypixelPacket> packetFactory) {
        packetRegistry.put(identifier, packetFactory);
    }

    public HypixelPacket createPacket(String identifier, PacketSerializer serializer) {
        Function<PacketSerializer, HypixelPacket> packetFactory = packetRegistry.get(identifier);
        if (packetFactory == null) {
            throw new IllegalArgumentException("Unknown packet identifier: " + identifier);
        }
        return packetFactory.apply(serializer);
    }

    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(packetRegistry.keySet());
    }

}
