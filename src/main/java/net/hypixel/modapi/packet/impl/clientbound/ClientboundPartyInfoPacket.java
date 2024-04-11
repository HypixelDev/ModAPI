package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.packet.HypixelPacketType;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ClientboundPartyInfoPacket extends VersionedPacket {
    private static final int CURRENT_VERSION = 1;

    private final boolean inParty;
    private final UUID leader;
    private final Set<UUID> members;

    public ClientboundPartyInfoPacket(boolean inParty, @Nullable UUID leader, Set<UUID> members) {
        super(CURRENT_VERSION);
        this.inParty = inParty;
        this.leader = leader;
        this.members = members;
    }

    public ClientboundPartyInfoPacket(PacketSerializer serializer) {
        super(serializer);

        this.inParty = serializer.readBoolean();
        if (!inParty) {
            this.leader = null;
            this.members = Collections.emptySet();
            return;
        }

        this.leader = serializer.readUuid();
        int memberCount = serializer.readVarInt();
        Set<UUID> members = new HashSet<>(memberCount);
        for (int i = 0; i < memberCount; i++) {
            members.add(serializer.readUuid());
        }
        this.members = Collections.unmodifiableSet(members);
    }

    @Override
    public HypixelPacketType getType() {
        return HypixelPacketType.PARTY_INFO;
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
