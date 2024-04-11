package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.packet.HypixelPacketType;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public class ServerboundPartyInfoPacket extends VersionedPacket {
    private static final byte CURRENT_VERSION = 1;

    public ServerboundPartyInfoPacket() {
        super(CURRENT_VERSION);
    }

    public ServerboundPartyInfoPacket(PacketSerializer serializer) {
        super(serializer);
    }

    @Override
    public HypixelPacketType getType() {
        return HypixelPacketType.PARTY_INFO;
    }
}
