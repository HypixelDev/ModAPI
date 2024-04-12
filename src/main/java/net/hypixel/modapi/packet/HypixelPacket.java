package net.hypixel.modapi.packet;

import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.serializer.PacketSerializer;

public interface HypixelPacket {

    void write(PacketSerializer serializer);

    default String getIdentifier() {
        return HypixelModAPI.getInstance().getRegistry().getIdentifier(getClass());
    }

}
