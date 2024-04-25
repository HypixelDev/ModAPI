package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.data.region.Environment;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientboundLocationPacket extends ClientboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    private Environment environment;
    private String proxyName;
    private String serverName;
    @Nullable
    private ServerType serverType;
    @Nullable
    private String lobbyName;
    @Nullable
    private String mode;
    @Nullable
    private String map;

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
    }

    @Override
    protected boolean read(PacketSerializer serializer) {
        if (!super.read(serializer)) {
            return false;
        }

        this.environment = Environment.getById(serializer.readVarInt()).orElseThrow(() -> new IllegalArgumentException("Invalid environment ID"));
        this.proxyName = serializer.readString();
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
        serializer.writeVarInt(environment.ordinal());
        serializer.writeString(proxyName);
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
