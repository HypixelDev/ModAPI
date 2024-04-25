package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ClientboundPartyInfoPacket extends ClientboundVersionedPacket {
    private static final int CURRENT_VERSION = 1;

    private boolean inParty;
    private UUID leader;
    private Set<UUID> members;

    public ClientboundPartyInfoPacket(boolean inParty, @Nullable UUID leader, Set<UUID> members) {
        super(CURRENT_VERSION);
        this.inParty = inParty;
        this.leader = leader;
        this.members = members;
    }

    public ClientboundPartyInfoPacket(PacketSerializer serializer) {
        super(serializer);
    }

    @Override
    protected boolean read(PacketSerializer serializer) {
        if (!super.read(serializer)) {
            return false;
        }

        this.inParty = serializer.readBoolean();
        if (!inParty) {
            this.leader = null;
            this.members = Collections.emptySet();
            return true;
        }

        this.leader = serializer.readUuid();
        int memberCount = serializer.readVarInt();
        Set<UUID> members = new HashSet<>(memberCount);
        for (int i = 0; i < memberCount; i++) {
            members.add(serializer.readUuid());
        }
        this.members = Collections.unmodifiableSet(members);
        return true;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);
        serializer.writeBoolean(inParty);
        if (!inParty) {
            return;
        }

        serializer.writeUuid(leader);
        serializer.writeVarInt(members.size());
        for (UUID member : members) {
            serializer.writeUuid(member);
        }
    }

    @Override
    protected int getLatestVersion() {
        return CURRENT_VERSION;
    }

    @Override
    public void handle(ClientboundPacketHandler handler) {
        handler.onPartyInfoPacket(this);
    }

    public boolean isInParty() {
        return inParty;
    }

    public Optional<UUID> getLeader() {
        return Optional.ofNullable(leader);
    }

    public Set<UUID> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "ClientboundPartyInfoPacket{" +
                "inParty=" + inParty +
                ", leader=" + leader +
                ", members=" + members +
                "} " + super.toString();
    }
}
