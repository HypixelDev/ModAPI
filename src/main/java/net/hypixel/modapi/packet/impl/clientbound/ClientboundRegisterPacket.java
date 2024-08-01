package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

/**
 * Is sent as a response to the {@link net.hypixel.modapi.packet.impl.serverbound.ServerboundRegisterPacket} when successfully registered.
 */
@ApiStatus.Internal
public class ClientboundRegisterPacket extends ClientboundVersionedPacket {
    private static final int CURRENT_VERSION = 2;

    private int requestIdentifier;

    public ClientboundRegisterPacket(int version, int requestIdentifier) {
        super(version);
        this.requestIdentifier = requestIdentifier;
        if (version != CURRENT_VERSION) {
            // Version 1 never existed, so we don't support sending it
            throw new IllegalArgumentException("Invalid version: " + version);
        }
    }

    public ClientboundRegisterPacket(PacketSerializer serializer) {
        super(serializer);
    }

    @Override
    protected boolean read(PacketSerializer serializer) {
        if (!super.read(serializer)) {
            return false;
        }

        this.requestIdentifier = serializer.readVarInt();
        return true;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeVarInt(requestIdentifier);
    }

    @Override
    protected int getLatestVersion() {
        return CURRENT_VERSION;
    }

    public int getRequestIdentifier() {
        return requestIdentifier;
    }

    @Override
    public String toString() {
        return "ClientboundRegisterPacket{" +
                "requestIdentifier=" + requestIdentifier +
                "} " + super.toString();
    }
}
