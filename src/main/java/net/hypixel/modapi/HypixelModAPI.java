package net.hypixel.modapi;

import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.error.ModAPIException;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.HypixelPacketType;
import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HypixelModAPI {
    private static final HypixelModAPI INSTANCE = new HypixelModAPI();

    public static HypixelModAPI getInstance() {
        return INSTANCE;
    }

    private final List<ClientboundPacketHandler> packetHandlers = new CopyOnWriteArrayList<>();

    private HypixelModAPI() {
    }

    public void registerHandler(ClientboundPacketHandler handler) {
        packetHandlers.add(handler);
    }

    public void handle(String identifier, PacketSerializer serializer) {
        if (packetHandlers.isEmpty()) {
            return;
        }

        HypixelPacketType packetType = HypixelPacketType.getByIdentifier(identifier);
        if (packetType == null) {
            return;
        }

        // All responses contain a boolean of if the response is a success, if not then a string is included with the error message
        if (!serializer.readBoolean()) {
            ErrorReason reason = ErrorReason.getById(serializer.readVarInt());
            throw new ModAPIException(packetType, reason);
        }

        HypixelPacket packet = packetType.getPacketFactory().apply(serializer);
        for (ClientboundPacketHandler handler : packetHandlers) {
            handler.handle(packet);
        }
    }

}
