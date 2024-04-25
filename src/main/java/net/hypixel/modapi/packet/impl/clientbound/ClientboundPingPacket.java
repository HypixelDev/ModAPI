package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public class ClientboundPingPacket extends ClientboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    private String response;

    public ClientboundPingPacket(String response) {
        super(CURRENT_VERSION);
        this.response = response;
    }

    public ClientboundPingPacket(PacketSerializer serializer) {
        super(serializer);
    }

    @Override
    protected boolean read(PacketSerializer serializer) {
        if (!super.read(serializer)) {
            return false;
        }

        this.response = serializer.readString();
        return true;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeString(response);
    }

    @Override
    protected int getLatestVersion() {
        return CURRENT_VERSION;
    }

    @Override
    public void handle(ClientboundPacketHandler handler) {
        handler.onPingPacket(this);
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
