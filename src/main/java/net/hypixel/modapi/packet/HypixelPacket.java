package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

public interface HypixelPacket {

    void write(PacketSerializer serializer);

    default String getIdentifier(PacketRegistry registry) {
        return registry.getIdentifier(getClass());
    }

}
