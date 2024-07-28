package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

public class ServerboundPartyInfoPacket extends ServerboundVersionedPacket {
    private static final int CURRENT_VERSION = 2;

    public ServerboundPartyInfoPacket() {
        super(CURRENT_VERSION);
    }

    @ApiStatus.Internal
    public ServerboundPartyInfoPacket(PacketSerializer serializer) {
        super(serializer);
    }

}
