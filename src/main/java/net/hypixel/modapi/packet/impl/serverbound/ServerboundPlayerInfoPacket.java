package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

public class ServerboundPlayerInfoPacket extends ServerboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    public ServerboundPlayerInfoPacket() {
        super(CURRENT_VERSION);
    }

    @ApiStatus.Internal
    public ServerboundPlayerInfoPacket(PacketSerializer serializer) {
        super(serializer);
    }

}
