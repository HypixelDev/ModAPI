package net.hypixel.modapi.packet.impl.clientbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket.PartyMember;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket.PartyRole;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class TestPartyInfoPacketDecoding {
    private static final int VERSION = 2;

    private static ClientboundPartyInfoPacket decode(Consumer<PacketSerializer> writer) {
        ByteBuf buf = Unpooled.buffer();
        try {
            PacketSerializer serializer = new PacketSerializer(buf);
            writer.accept(serializer);
            return new ClientboundPartyInfoPacket(serializer);
        } finally {
            buf.release();
        }
    }

    @Test
    void testValidPacketDecodes() {
        Map<UUID, PartyMember> members = new HashMap<>();
        UUID leader = UUID.randomUUID();
        UUID member = UUID.randomUUID();
        members.put(leader, new PartyMember(leader, PartyRole.LEADER));
        members.put(member, new PartyMember(member, PartyRole.MEMBER));

        ClientboundPartyInfoPacket packet = decode(new ClientboundPartyInfoPacket(VERSION, true, members)::write);

        Assertions.assertEquals(2, packet.getMemberMap().size());
        Assertions.assertEquals(leader, packet.getLeader().orElse(null));
        Assertions.assertEquals(PartyRole.MEMBER, packet.getMemberMap().get(member).getRole());
    }

    @Test
    void testMinSerializedBytesMatchesSmallestMember() {
        int smallest = Integer.MAX_VALUE;
        for (PartyRole role : PartyRole.values()) {
            ByteBuf buf = Unpooled.buffer();
            try {
                new PartyMember(UUID.randomUUID(), role).write(new PacketSerializer(buf));
                smallest = Math.min(smallest, buf.readableBytes());
            } finally {
                buf.release();
            }
        }

        Assertions.assertEquals(smallest, PartyMember.MIN_SERIALIZED_BYTES,
                "PartyMember.MIN_SERIALIZED_BYTES is out of date with PartyMember#write");
    }

    @Test
    void testOversizedMemberCountIsRejected() {
        Assertions.assertThrows(DecoderException.class, () -> decode(serializer -> {
            serializer.writeVarInt(VERSION);
            serializer.writeBoolean(true);
            serializer.writeVarInt(Integer.MAX_VALUE);
            // HashMap allocates its table on the first put, so one real member is what triggered the OOM
            serializer.writeUuid(UUID.randomUUID());
            serializer.writeVarInt(PartyRole.LEADER.ordinal());
        }));
    }

    @Test
    void testMemberCountLargerThanPayloadIsRejected() {
        Assertions.assertThrows(DecoderException.class, () -> decode(serializer -> {
            serializer.writeVarInt(VERSION);
            serializer.writeBoolean(true);
            serializer.writeVarInt(2);
            serializer.writeUuid(UUID.randomUUID());
            serializer.writeVarInt(PartyRole.LEADER.ordinal());
        }));
    }

    @Test
    void testNegativeMemberCountIsRejected() {
        Assertions.assertThrows(DecoderException.class, () -> decode(serializer -> {
            serializer.writeVarInt(VERSION);
            serializer.writeBoolean(true);
            serializer.writeVarInt(-1);
        }));
    }

    @Test
    void testUnknownRoleIsRejected() {
        Assertions.assertThrows(DecoderException.class, () -> decode(serializer -> {
            serializer.writeVarInt(VERSION);
            serializer.writeBoolean(true);
            serializer.writeVarInt(1);
            serializer.writeUuid(UUID.randomUUID());
            serializer.writeVarInt(PartyRole.values().length);
        }));
    }
}
