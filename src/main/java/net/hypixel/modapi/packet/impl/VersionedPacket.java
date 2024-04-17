package net.hypixel.modapi.packet.impl;

import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

/**
 * Represents a packet that is backed by a version. Versioned packets will only be handled if the incoming packet matches the version of the packet known.
 */
public abstract class VersionedPacket implements HypixelPacket {
    private int version;

    public VersionedPacket(int version) {
        this.version = version;
    }

    public VersionedPacket(PacketSerializer serializer) {
        read(serializer);
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

    public int getVersion() {
        return version;
    }

    protected abstract int getLatestVersion();

    public boolean isExpectedVersion() {
        return getVersion() == getLatestVersion();
    }

    @Override
    public String toString() {
        return "VersionedPacket{" +
                "version=" + version +
                '}';
    }
}
