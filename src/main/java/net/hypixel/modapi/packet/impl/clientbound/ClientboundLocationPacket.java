package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.data.Environment;
import net.hypixel.modapi.packet.HypixelPacketType;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientboundLocationPacket extends VersionedPacket {
    private static final byte CURRENT_VERSION = 1;

    private final Environment environment;
    private final String proxyName;
    private final String serverName;
    @Nullable
    private final String serverType;
    @Nullable
    private final String lobbyName;
    @Nullable
    private final String mode;
    @Nullable
    private final String map;

    public ClientboundLocationPacket(Environment environment, String proxyName, String serverName, @Nullable String serverType, @Nullable String lobbyName, @Nullable String mode, @Nullable String map) {
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
        this.environment = Environment.VALUES[serializer.readVarInt()];
        this.proxyName = serializer.readString();
        this.serverName = serializer.readString();
        this.serverType = serializer.readBoolean() ? serializer.readString() : null;
        this.lobbyName = serializer.readBoolean() ? serializer.readString() : null;
        this.mode = serializer.readBoolean() ? serializer.readString() : null;
        this.map = serializer.readBoolean() ? serializer.readString() : null;
    }

    @Override
    public HypixelPacketType getType() {
        return HypixelPacketType.LOCATION;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeVarInt(environment.ordinal());
        serializer.writeString(proxyName);
        serializer.writeString(serverName);

        serializer.writeBoolean(serverType != null);
        if (serverType != null) {
            serializer.writeString(serverType);
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

    public Environment getEnvironment() {
        return environment;
    }

    public String getProxyName() {
        return proxyName;
    }

    public String getServerName() {
        return serverName;
    }

    public Optional<String> getServerType() {
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
