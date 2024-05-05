package net.hypixel.modapi.packet;

import net.hypixel.modapi.annotation.Experimental;

/**
 * Represents a packet that is automatically sent based automatically based on specific conditions.
 * <p>
 * Event driven packets do not have a serverbound instance, as they can only be triggered by the server.
 * You will however, need to subscribe to wanted events, see {@link net.hypixel.modapi.HypixelModAPI#subscribeToEventPacket(Class)}.
 */
@Experimental
public interface EventPacket extends ClientboundHypixelPacket {
}
