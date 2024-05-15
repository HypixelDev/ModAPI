package net.hypixel.modapi.handler;

import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;

public interface ClientboundPacketHandler {

    default void onHelloEvent(ClientboundHelloPacket packet) {
    }

    default void onPingPacket(ClientboundPingPacket packet) {
    }

    default void onPartyInfoPacket(ClientboundPartyInfoPacket packet) {
    }

    default void onPlayerInfoPacket(ClientboundPlayerInfoPacket packet) {
    }

    default void onLocationEvent(ClientboundLocationPacket packet) {
    }
}
