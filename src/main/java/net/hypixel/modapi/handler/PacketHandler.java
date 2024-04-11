package net.hypixel.modapi.handler;

import net.hypixel.modapi.packet.HypixelPacket;

public interface PacketHandler {

    void handle(HypixelPacket packet);

}
