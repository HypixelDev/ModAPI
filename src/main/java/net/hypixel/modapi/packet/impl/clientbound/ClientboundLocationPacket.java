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
        this.serverType = serializer.readBoolean() ? ServerType.valueOf(serializer.readString()).orElse(null) : null;
        this.lobbyName = serializer.readBoolean() ? serializer.readString() : null;
        this.mode = serializer.readBoolean() ? serializer.readString() : null;
        this.map = serializer.readBoolean() ? serializer.readString() : null;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeVarInt(environment.ordinal());
        serializer.writeString(proxyName);
        serializer.writeString(serverName);

        serializer.writeBoolean(serverType != null);
        if (serverType != null) {
            serializer.writeString(serverType.name());
        }

        serializer.writeBoolean(lobbyName != null);
        if (lobbyName != null) {
            serializer.writeString(lobbyName);
        }

        serializer.writeBoolean(mode != null);
        if (mode != null) {
            serializer.writeString(mode);
        }

        serializer.writeBoolean(map != null);
        if (map != null) {
            serializer.writeString(map);
        }
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
