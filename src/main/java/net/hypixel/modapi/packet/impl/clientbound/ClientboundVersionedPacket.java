package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

/**
 * Represents a packet that is backed by a version. Clientbound versioned packets will only be handled if the incoming packet matches the version of the packet known.
 */
public abstract class ClientboundVersionedPacket extends VersionedPacket implements ClientboundHypixelPacket {

    public ClientboundVersionedPacket(int version) {
        super(version);
    }

    public ClientboundVersionedPacket(PacketSerializer serializer) {
        super(serializer);
    }

    /**
     * @return true if reading was successful, false if otherwise (such as due to a mismatch in version)
     */
    protected boolean read(PacketSerializer serializer) {
        this.version = serializer.readVarInt();
        return isExpectedVersion();
    }

    @Override
    public void write(PacketSerializer serializer) {
        serializer.writeVarInt(version);
    }

    protected abstract int getLatestVersion();

    public boolean isExpectedVersion() {
        return getVersion() == getLatestVersion();
    }

}
