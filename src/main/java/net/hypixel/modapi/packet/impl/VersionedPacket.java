package net.hypixel.modapi.packet.impl;

import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a packet that is backed by a version. Versioned packets will only be handled if the incoming packet matches the version of the packet known.
 */
public abstract class VersionedPacket implements HypixelPacket {
    protected int version;

    @ApiStatus.Internal
    public VersionedPacket(int version) {
        this.version = version;
    }

    @ApiStatus.Internal
    public VersionedPacket(PacketSerializer serializer) {
        read(serializer);
    }

    /**
     * @return true if reading was successful, false if otherwise (such as due to a mismatch in version)
     */
    protected abstract boolean read(PacketSerializer serializer);

    @Override
    public void write(PacketSerializer serializer) {
        serializer.writeVarInt(version);
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "VersionedPacket{" +
                "version=" + version +
                '}';
    }
}
