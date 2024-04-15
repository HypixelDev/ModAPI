package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.data.region.Environment;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientboundLocationPacket extends VersionedPacket implements ClientboundHypixelPacket {
    private static final int CURRENT_VERSION = 1;

    private final Environment environment;
    private final String proxyName;
    private final String serverName;
    @Nullable
    private final ServerType serverType;
    @Nullable
    private final String lobbyName;
    @Nullable
    private final String mode;
    @Nullable
    private final String map;

    public ClientboundLocationPacket(Environment environment, String proxyName, String serverName, @Nullable ServerType serverType, @Nullable String lobbyName, @Nullable String mode, @Nullable String map) {
        super(CURRENT_VERSION);
        this.environment = environment;
        this.proxyName = proxyName;
        this.serverName = serverName;
        this.serverType = serverType;
        this.lobbyName = lobbyName;
        this.mode = mode;
        this.map = map;
    }

    public ClientboundLocationPacket(PacketSerializer serializer) {
        super(serializer);
        this.environment = Environment.getById(serializer.readVarInt()).orElseThrow(() -> new IllegalArgumentException("Invalid environment ID"));
        this.proxyName = serializer.readString();
        this.serverName = serializer.readString();
        this.serverType = serializer.readOptional(PacketSerializer::readString).flatMap(ServerType::valueOf).orElse(null);
        this.lobbyName = serializer.readOptionally(PacketSerializer::readString);
        this.mode = serializer.readOptionally(PacketSerializer::readString);
        this.map = serializer.readOptionally(PacketSerializer::readString);
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeVarInt(environment.ordinal());
        serializer.writeString(proxyName);
        serializer.writeString(serverName);
        serializer.writeOptionally(serverType, (s, t) -> s.writeString(t.name()));
        serializer.writeOptionally(lobbyName, PacketSerializer::writeString);
        serializer.writeOptionally(mode, PacketSerializer::writeString);
        serializer.writeOptionally(map, PacketSerializer::writeString);
    }

    @Override
    public void handle(ClientboundPacketHandler handler) {
        handler.onLocationPacket(this);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getProxyName() {
        return proxyName;
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
                "environment=" + environment +
                ", proxyName='" + proxyName + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverType='" + serverType + '\'' +
                ", lobbyName='" + lobbyName + '\'' +
                ", mode='" + mode + '\'' +
                ", map='" + map + '\'' +
                "} " + super.toString();
    }
}
