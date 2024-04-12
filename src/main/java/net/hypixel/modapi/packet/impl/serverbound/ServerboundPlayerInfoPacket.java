package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

public class ServerboundPlayerInfoPacket extends VersionedPacket {
    private static final int CURRENT_VERSION = 1;

    public ServerboundPlayerInfoPacket() {
        super(CURRENT_VERSION);
    }

    public ServerboundPlayerInfoPacket(PacketSerializer serializer) {
        super(serializer);
    }

}
