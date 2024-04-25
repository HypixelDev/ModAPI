package net.hypixel.modapi.packet.handler;

import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.PacketHandler;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket;

import java.util.Collections;

public class PartyInfoHandler implements PacketHandler<ServerboundPartyInfoPacket> {
    @Override
    public ClientboundHypixelPacket handle(String identifier, ServerboundPartyInfoPacket packet) {
        return new ClientboundPartyInfoPacket(
                2,
                false,
                Collections.emptyMap()
        );
    }
}
