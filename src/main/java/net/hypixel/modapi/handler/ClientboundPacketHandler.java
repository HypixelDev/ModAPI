package net.hypixel.modapi.handler;

import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;

public interface ClientboundPacketHandler {

    default void onPingPacket(ClientboundPingPacket packet) {
    }

    default void onLocationPacket(ClientboundLocationPacket packet) {
    }

    default void onPartyInfoPacket(ClientboundPartyInfoPacket packet) {
    }

    default void onPlayerInfoPacket(ClientboundPlayerInfoPacket packet) {
    }
}
