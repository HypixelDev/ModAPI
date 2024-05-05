package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.annotation.Experimental;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

/**
 * This packet is automatically sent on every join to Hypixel to indicate that the client has connected to a Hypixel server.
 * As a result of this, the packet contains no version or data to be serialized and is simply a marker.
 */
@Experimental
public class ClientboundHelloPacket implements ClientboundHypixelPacket {

    public ClientboundHelloPacket() {
    }

    public ClientboundHelloPacket(PacketSerializer serializer) {
    }

    @Override
    public void handle(ClientboundPacketHandler handler) {
        handler.onHelloEvent(this);
    }

    @Override
    public void write(PacketSerializer serializer) {
    }

    @Override
    public String toString() {
        return "ClientboundHelloPacket{}";
    }
}
