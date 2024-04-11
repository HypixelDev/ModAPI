package net.hypixel.modapi.packet.impl;

import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public abstract class VersionedPacket implements HypixelPacket {
    private final byte version;

    public VersionedPacket(byte version) {
        this.version = version;
    }

    public VersionedPacket(PacketSerializer byteBuf) {
        this.version = byteBuf.readByte();
    }

    @Override
    public void write(PacketSerializer serializer) {
        serializer.writeByte(version);
    }

    public byte getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "VersionedPacket{" +
                "version=" + version +
                '}';
    }
}
