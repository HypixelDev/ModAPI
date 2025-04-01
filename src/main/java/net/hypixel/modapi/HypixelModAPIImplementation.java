package net.hypixel.modapi;

import net.hypixel.modapi.packet.HypixelPacket;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface HypixelModAPIImplementation {

    void onInit();

    boolean sendPacket(HypixelPacket packet);

    boolean isConnectedToHypixel();

}
