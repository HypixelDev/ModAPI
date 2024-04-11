package net.hypixel.modapi.packet;

import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum HypixelPacketType {
    PING(ClientboundPingPacket::new),
    LOCATION(ClientboundLocationPacket::new),
    PARTY_INFO(ClientboundPartyInfoPacket::new),
    PLAYER_INFO(ClientboundPlayerInfoPacket::new),
    ;
    private static final String IDENTIFIER_PREFIX = "hypixel:";
    private static final Map<String, HypixelPacketType> BY_IDENTIFIER = Arrays.stream(values()).collect(HashMap::new, (map, type) -> map.put(type.getIdentifier(), type), HashMap::putAll);
    private final Function<PacketSerializer, HypixelPacket> packetFactory;

    @Nullable
    public static HypixelPacketType getByIdentifier(String identifier) {
        return BY_IDENTIFIER.get(identifier);
    }

    private final String identifier;

    HypixelPacketType(Function<PacketSerializer, HypixelPacket> packetFactory) {
        this.identifier = IDENTIFIER_PREFIX + name().toLowerCase();
        this.packetFactory = packetFactory;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Function<PacketSerializer, HypixelPacket> getPacketFactory() {
        return packetFactory;
    }
}
