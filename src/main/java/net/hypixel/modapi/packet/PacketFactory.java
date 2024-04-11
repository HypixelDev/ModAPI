package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

public interface PacketFactory<P extends HypixelPacket> {

	P create(PacketSerializer serializer);
}
