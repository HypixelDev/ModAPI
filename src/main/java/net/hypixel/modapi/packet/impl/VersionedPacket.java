package net.hypixel.modapi.packet.impl;

import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public abstract class VersionedPacket implements HypixelPacket {
    private final int version;

    public VersionedPacket(int version) {
        this.version = version;
    }

    public VersionedPacket(PacketSerializer byteBuf) {
        this.version = byteBuf.readVarInt();
    }

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
