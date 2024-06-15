package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.*;

public class ClientboundPartyInfoPacket extends ClientboundVersionedPacket {
    private static final int CURRENT_VERSION = 2;

    private boolean inParty;
    private Map<UUID, PartyMember> memberMap;

    public ClientboundPartyInfoPacket(int version, boolean inParty, Map<UUID, PartyMember> memberMap) {
        super(version);
        if (version > CURRENT_VERSION) {
            throw new IllegalArgumentException("Version " + version + " is greater than the current version " + CURRENT_VERSION);
        }

        this.inParty = inParty;
        this.memberMap = memberMap;
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
            this.memberMap = Collections.emptyMap();
            return true;
        }

        int memberCount = serializer.readVarInt();
        Map<UUID, PartyMember> memberMap = new HashMap<>(memberCount);
        for (int i = 0; i < memberCount; i++) {
            PartyMember member = new PartyMember(serializer);
            memberMap.put(member.getUuid(), member);
        }
        this.memberMap = Collections.unmodifiableMap(memberMap);
        return true;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);

        if (version == 1) {
            Optional<UUID> leader = getLeader();
            if (!leader.isPresent()) {
                serializer.writeBoolean(false);
                return;
            }

            serializer.writeBoolean(true);
            serializer.writeUuid(leader.get());
            Set<UUID> members = getMembers();
            serializer.writeVarInt(members.size());
            for (UUID member : members) {
                serializer.writeUuid(member);
            }
            return;
        }

        serializer.writeBoolean(inParty);
        if (!inParty) {
            return;
        }

        serializer.writeVarInt(memberMap.size());
        for (PartyMember member : memberMap.values()) {
            member.write(serializer);
        }
    }

    @Override
    protected int getLatestVersion() {
        return CURRENT_VERSION;
    }

    public boolean isInParty() {
        return inParty;
    }

    public Optional<UUID> getLeader() {
        if (!inParty) {
            return Optional.empty();
        }
        return memberMap.values().stream()
                .filter(member -> member.getRole() == PartyRole.LEADER)
                .map(PartyMember::getUuid)
                .findFirst();
    }

    public Set<UUID> getMembers() {
        return memberMap.keySet();
    }

    public Map<UUID, PartyMember> getMemberMap() {
        return memberMap;
    }

    @Override
    public String toString() {
        return "ClientboundPartyInfoPacket{" +
                "inParty=" + inParty +
                ", memberMap=" + memberMap +
                "} " + super.toString();
    }

    public static class PartyMember {
        private final UUID uuid;
        private final PartyRole role;

        public PartyMember(UUID uuid, PartyRole role) {
            this.uuid = uuid;
            this.role = role;
        }

        PartyMember(PacketSerializer serializer) {
            this.uuid = serializer.readUuid();
            this.role = PartyRole.VALUES[serializer.readVarInt()];
        }

        void write(PacketSerializer serializer) {
            serializer.writeUuid(uuid);
            serializer.writeVarInt(role.ordinal());
        }

        public UUID getUuid() {
            return uuid;
        }

        public PartyRole getRole() {
            return role;
        }

        @Override
        public String toString() {
            return "PartyMember{" +
                    "uuid=" + uuid +
                    ", role=" + role +
                    '}';
        }
    }

    public enum PartyRole {
        LEADER,
        MOD,
        MEMBER,
        ;

        private static final PartyRole[] VALUES = values();
    }
}
