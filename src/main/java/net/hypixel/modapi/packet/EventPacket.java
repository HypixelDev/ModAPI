package net.hypixel.modapi.packet;

import net.hypixel.modapi.annotation.Experimental;

/**
 * Represents a packet that is automatically sent based automatically based on specific conditions.
 * <p>
 * Event driven packets do not have a serverbound instance, as they can only be triggered by the server.
 * You will however, need to register that you wish to receive an event driven packet, see {@link net.hypixel.modapi.packet.impl.serverbound.ServerboundRegisterPacket}.
 */
@Experimental
public interface EventPacket {
}
