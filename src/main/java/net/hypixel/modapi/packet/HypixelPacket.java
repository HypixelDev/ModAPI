package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

public interface HypixelPacket {

    HypixelPacketType getType();

    void write(PacketSerializer serializer);

}
