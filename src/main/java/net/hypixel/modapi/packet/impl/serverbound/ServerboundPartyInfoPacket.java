package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.serializer.PacketSerializer;

public class ServerboundPartyInfoPacket extends ServerboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    public ServerboundPartyInfoPacket() {
        super(CURRENT_VERSION);
    }

    public ServerboundPartyInfoPacket(PacketSerializer serializer) {
        super(serializer);
    }

}
