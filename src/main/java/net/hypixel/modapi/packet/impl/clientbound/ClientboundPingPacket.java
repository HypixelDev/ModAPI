package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public class ClientboundPingPacket extends VersionedPacket implements ClientboundHypixelPacket {
    private static final int CURRENT_VERSION = 1;

    private final String response;

    public ClientboundPingPacket(String response) {
        super(CURRENT_VERSION);
        this.response = response;
    }

    public ClientboundPingPacket(PacketSerializer serializer) {
        super(serializer);
        this.response = serializer.readString();
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeString(response);
    }

    @Override
    public void handle(ClientboundPacketHandler handler) {
        handler.handle(this);
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "ClientboundPingPacket{" +
                "response='" + response + '\'' +
                "} " + super.toString();
    }
}
