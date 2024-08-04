package net.hypixel.modapi;

import net.hypixel.modapi.packet.HypixelPacket;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface HypixelModImplementation {

    boolean sendPacket(HypixelPacket packet);

    boolean isConnectedToHypixel();

}
