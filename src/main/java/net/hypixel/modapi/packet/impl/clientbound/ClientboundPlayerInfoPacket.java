package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.data.rank.MonthlyPackageRank;
import net.hypixel.data.rank.PackageRank;
import net.hypixel.data.rank.PlayerRank;
import net.hypixel.modapi.packet.HypixelPacketType;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientboundPlayerInfoPacket extends VersionedPacket {
    private static final byte CURRENT_VERSION = 1;

    private final PlayerRank playerRank;
    private final PackageRank packageRank;
    private final MonthlyPackageRank monthlyPackageRank;
    @Nullable
    private final String prefix;

    public ClientboundPlayerInfoPacket(PlayerRank playerRank, PackageRank packageRank, MonthlyPackageRank monthlyPackageRank, @Nullable String prefix) {
        super(CURRENT_VERSION);
        this.playerRank = playerRank;
        this.packageRank = packageRank;
        this.monthlyPackageRank = monthlyPackageRank;
        this.prefix = prefix;
    }

    public ClientboundPlayerInfoPacket(PacketSerializer serializer) {
        super(serializer);
        this.playerRank = PlayerRank.getById(serializer.readByte()).orElseThrow(() -> new IllegalArgumentException("Invalid player rank ID"));
        this.packageRank = PackageRank.getById(serializer.readByte()).orElseThrow(() -> new IllegalArgumentException("Invalid package rank ID"));
        this.monthlyPackageRank = MonthlyPackageRank.getById(serializer.readByte()).orElseThrow(() -> new IllegalArgumentException("Invalid monthly package rank ID"));
        this.prefix = serializer.readBoolean() ? serializer.readString() : null;
    }

    @Override
    public HypixelPacketType getType() {
        return HypixelPacketType.PLAYER_INFO;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeByte(playerRank.getId());
        serializer.writeByte(packageRank.getId());
        serializer.writeByte(monthlyPackageRank.getId());
        serializer.writeBoolean(prefix != null);
        if (prefix != null) {
            serializer.writeString(prefix);
        }
    }

    public PlayerRank getPlayerRank() {
        return playerRank;
    }

    public PackageRank getPackageRank() {
        return packageRank;
    }

    public MonthlyPackageRank getMonthlyPackageRank() {
        return monthlyPackageRank;
    }

    public Optional<String> getPrefix() {
        return Optional.ofNullable(prefix);
    }

    @Override
    public String toString() {
        return "ClientboundPlayerInfoPacket{" +
                "playerRank=" + playerRank +
                ", packageRank=" + packageRank +
                ", monthlyPackageRank=" + monthlyPackageRank +
                ", prefix='" + prefix + '\'' +
                "} " + super.toString();
    }
}
