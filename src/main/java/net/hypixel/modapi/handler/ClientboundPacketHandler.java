package net.hypixel.modapi.handler;

import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationEventPacket;

public interface ClientboundPacketHandler {

    default void onPingPacket(ClientboundPingPacket packet) {
    }

    default void onLocationPacket(ClientboundLocationPacket packet) {
    }

    default void onPartyInfoPacket(ClientboundPartyInfoPacket packet) {
    }

    default void onPlayerInfoPacket(ClientboundPlayerInfoPacket packet) {
    }

    default void onHelloEvent(ClientboundHelloPacket packet) {
    }

    default void onLocationEvent(ClientboundLocationEventPacket packet) {
    }
}
