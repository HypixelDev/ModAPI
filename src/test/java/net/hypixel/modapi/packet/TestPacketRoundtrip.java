package net.hypixel.modapi.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundRegisterPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class TestPacketRoundtrip {
    private static final ServerboundPacketHandler HANDLER = new ServerboundPacketHandler();

    private static ClientboundHypixelPacket handleServerbound(String identifier, PacketSerializer serializer) {
        HypixelPacket packet = HypixelModAPI.getInstance().getRegistry().createServerboundPacket(identifier, serializer);
        if (packet == null) {
            throw new IllegalArgumentException("Unknown packet identifier: " + identifier);
        }

        return HANDLER.handle(identifier, packet);
    }

    private static ClientboundHypixelPacket doPacketRoundtrip(HypixelPacket packet) {
        ByteBuf buf = Unpooled.buffer();
        try {
            PacketSerializer serializer = new PacketSerializer(buf);
            packet.write(serializer);
            return handleServerbound(packet.getIdentifier(), serializer);
        } finally {
            buf.release();
        }
    }

    private static Stream<Arguments> packetProvider() {
        return HypixelModAPI.getInstance().getRegistry().getRegistrations().stream()
                .filter(registration -> registration.getServerboundClazz() != null)
                .filter(registration -> registration.getClientboundClazz() != null)
                // Exclude register packet as it's not a normal packet and doesn't have an empty constructor
                .filter(registration -> !registration.getServerboundClazz().equals(ServerboundRegisterPacket.class))
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("packetProvider")
    void testPacketRoundtrip(PacketRegistry.Registration registration) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        HypixelPacket serverbound = registration.getServerboundClazz().getConstructor().newInstance();
        ClientboundHypixelPacket response = doPacketRoundtrip(serverbound);
        Assertions.assertNotNull(response, "No response for packet " + registration.getServerboundClazz().getSimpleName());
        Assertions.assertEquals(registration.getClientboundClazz(), response.getClass(), "Unexpected response for packet " + registration.getServerboundClazz().getSimpleName());
    }

}
