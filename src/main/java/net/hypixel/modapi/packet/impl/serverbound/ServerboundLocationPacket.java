package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.serializer.PacketSerializer;

public class ServerboundLocationPacket extends ServerboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    public ServerboundLocationPacket() {
        super(CURRENT_VERSION);
    }

    public ServerboundLocationPacket(PacketSerializer serializer) {
        super(serializer);
    }

}
