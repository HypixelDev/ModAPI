package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.packet.HypixelPacketType;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public class ClientboundPingPacket extends VersionedPacket {
    private static final byte CURRENT_VERSION = 1;

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
    public HypixelPacketType getType() {
        return HypixelPacketType.PING;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeString(response);
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
