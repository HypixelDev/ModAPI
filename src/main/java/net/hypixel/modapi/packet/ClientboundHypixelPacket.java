package net.hypixel.modapi.packet;

import net.hypixel.modapi.handler.ClientboundPacketHandler;

public interface ClientboundHypixelPacket extends HypixelPacket {

	void handle(ClientboundPacketHandler handler);

}
