package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    public Function<PacketSerializer, HypixelPacket> getPacketFactory(String identifier) {
        return packetRegistry.get(identifier);
    }

    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(packetRegistry.keySet());
    }

}
