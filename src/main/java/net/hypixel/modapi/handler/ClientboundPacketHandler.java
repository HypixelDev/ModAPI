package net.hypixel.modapi.handler;

import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;

public interface ClientboundPacketHandler extends PacketHandler {

    default void handle(HypixelPacket packet) {
        if (packet instanceof ClientboundPingPacket) {
            onPingPacket((ClientboundPingPacket) packet);
        }

        if (packet instanceof ClientboundLocationPacket) {
            onLocationPacket((ClientboundLocationPacket) packet);
        }

        if (packet instanceof ClientboundPartyInfoPacket) {
            onPartyInfoPacket((ClientboundPartyInfoPacket) packet);
        }

        if (packet instanceof ClientboundPlayerInfoPacket) {
            onPlayerInfoPacket((ClientboundPlayerInfoPacket) packet);
        }
    }

    default void onPingPacket(ClientboundPingPacket packet) {
    }

    default void onLocationPacket(ClientboundLocationPacket packet) {
    }

    default void onPartyInfoPacket(ClientboundPartyInfoPacket packet) {
    }

    default void onPlayerInfoPacket(ClientboundPlayerInfoPacket packet) {
    }
}
