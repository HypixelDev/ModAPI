package net.hypixel.modapi.packet;

import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

public interface HypixelPacket {

    @ApiStatus.Internal
    void write(PacketSerializer serializer);

    @ApiStatus.Internal
    default String getIdentifier() {
        return HypixelModAPI.getInstance().getRegistry().getIdentifier(getClass());
    }

}
