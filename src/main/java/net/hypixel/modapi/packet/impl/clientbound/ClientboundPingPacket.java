package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

public class ClientboundPingPacket extends ClientboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    private String response;

    @ApiStatus.Internal
    public ClientboundPingPacket(String response) {
        super(CURRENT_VERSION);
        this.response = response;
    }

    @ApiStatus.Internal
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
