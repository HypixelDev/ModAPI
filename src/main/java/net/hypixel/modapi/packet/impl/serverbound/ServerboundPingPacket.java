package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.serializer.PacketSerializer;

public class ServerboundPingPacket extends ServerboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    public ServerboundPingPacket() {
        super(CURRENT_VERSION);
    }

    public ServerboundPingPacket(PacketSerializer serializer) {
        super(serializer);
    }

}
