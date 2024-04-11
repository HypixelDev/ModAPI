package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.packet.HypixelPacketType;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public class ServerboundPingPacket extends VersionedPacket {
    private static final byte CURRENT_VERSION = 1;

    public ServerboundPingPacket() {
        super(CURRENT_VERSION);
    }

    public ServerboundPingPacket(PacketSerializer serializer) {
        super(serializer);
    }

    @Override
    public HypixelPacketType getType() {
        return HypixelPacketType.PING;
    }
}
