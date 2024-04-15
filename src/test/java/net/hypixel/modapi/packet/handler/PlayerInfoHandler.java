package net.hypixel.modapi.packet.handler;

import net.hypixel.data.rank.MonthlyPackageRank;
import net.hypixel.data.rank.PackageRank;
import net.hypixel.data.rank.PlayerRank;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.PacketHandler;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPlayerInfoPacket;

public class PlayerInfoHandler implements PacketHandler<ServerboundPlayerInfoPacket> {
    @Override
    public ClientboundHypixelPacket handle(String identifier, ServerboundPlayerInfoPacket packet) {
        return new ClientboundPlayerInfoPacket(
                PlayerRank.NORMAL,
                PackageRank.MVP_PLUS,
                MonthlyPackageRank.SUPERSTAR,
                null
        );
    }
}
