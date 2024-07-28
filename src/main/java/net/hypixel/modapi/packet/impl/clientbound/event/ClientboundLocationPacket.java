package net.hypixel.modapi.packet.impl.clientbound.event;

import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.packet.EventPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundVersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientboundLocationPacket extends ClientboundVersionedPacket implements EventPacket {
    public static final int CURRENT_VERSION = 1;

    private String serverName;
    @Nullable
    private ServerType serverType;
    @Nullable
    private String lobbyName;
    @Nullable
    private String mode;
    @Nullable
    private String map;

    @ApiStatus.Internal
    public ClientboundLocationPacket(String serverName, @Nullable ServerType serverType, @Nullable String lobbyName, @Nullable String mode, @Nullable String map) {
        super(CURRENT_VERSION);
        this.serverName = serverName;
        this.serverType = serverType;
        this.lobbyName = lobbyName;
        this.mode = mode;
        this.map = map;
    }

    @ApiStatus.Internal
    public ClientboundLocationPacket(PacketSerializer serializer) {
        super(serializer);
    }

    @Override
    protected boolean read(PacketSerializer serializer) {
        if (!super.read(serializer)) {
            return false;
        }

        this.serverName = serializer.readString();
        this.serverType = serializer.readOptional(PacketSerializer::readString).flatMap(ServerType::valueOf).orElse(null);
        this.lobbyName = serializer.readOptionally(PacketSerializer::readString);
        this.mode = serializer.readOptionally(PacketSerializer::readString);
        this.map = serializer.readOptionally(PacketSerializer::readString);
        return true;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeString(serverName);
        serializer.writeOptionally(serverType, (s, t) -> s.writeString(t.name()));
        serializer.writeOptionally(lobbyName, PacketSerializer::writeString);
        serializer.writeOptionally(mode, PacketSerializer::writeString);
        serializer.writeOptionally(map, PacketSerializer::writeString);
    }

    @Override
    protected int getLatestVersion() {
        return CURRENT_VERSION;
    }

    public String getServerName() {
        return serverName;
    }

    public Optional<ServerType> getServerType() {
        return Optional.ofNullable(serverType);
    }

    public Optional<String> getLobbyName() {
        return Optional.ofNullable(lobbyName);
    }

    public Optional<String> getMode() {
        return Optional.ofNullable(mode);
    }

    public Optional<String> getMap() {
        return Optional.ofNullable(map);
    }

    @Override
    public String toString() {
        return "ClientboundLocationPacket{" +
                "serverName='" + serverName + '\'' +
                ", serverType='" + serverType + '\'' +
                ", lobbyName='" + lobbyName + '\'' +
                ", mode='" + mode + '\'' +
                ", map='" + map + '\'' +
                "} " + super.toString();
    }
}
