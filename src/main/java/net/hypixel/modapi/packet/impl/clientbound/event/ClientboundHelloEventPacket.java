package net.hypixel.modapi.packet.impl.clientbound.event;

import net.hypixel.modapi.annotation.Experimental;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.EventPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

/**
 * This packet is automatically sent on every join to Hypixel to indicate that the client has connected to a Hypixel server.
 * There is no need to register this packet in {@link net.hypixel.modapi.packet.impl.serverbound.ServerboundRegisterPacket} as it is automatically sent regardless of registration.
 * As a result of this, the packet contains no version or data to be serialized and is simply a marker.
 */
@Experimental
public class ClientboundHelloEventPacket implements ClientboundHypixelPacket, EventPacket {

    public ClientboundHelloEventPacket() {
    }

    public ClientboundHelloEventPacket(PacketSerializer serializer) {
    }

    @Override
    public void handle(ClientboundPacketHandler handler) {
        handler.onHelloEvent(this);
    }

    @Override
    public void write(PacketSerializer serializer) {

    }
}
