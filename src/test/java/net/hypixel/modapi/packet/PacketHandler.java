package net.hypixel.modapi.packet;

import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.HypixelPacket;

public interface PacketHandler<T extends HypixelPacket> {

    ClientboundHypixelPacket handle(String identifier, T packet);

}
