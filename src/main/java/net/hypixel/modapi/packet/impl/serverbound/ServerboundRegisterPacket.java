package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Notifys the remote server what versions of event packets we want to receive.
 * <p>
 * You should not use this packet manually, instead, use {@link net.hypixel.modapi.HypixelModAPI#subscribeToEventPacket(Class)} to subscribe to event packets.
 */
@ApiStatus.Internal
public class ServerboundRegisterPacket extends ServerboundVersionedPacket {
    private static final int MAX_IDENTIFIER_LENGTH = 20;
    private static final int MAX_IDENTIFIERS = 5;
    private static final int CURRENT_VERSION = 1;

    private Map<String, Integer> subscribedEvents;

    public ServerboundRegisterPacket(Map<String, Integer> subscribedEvents) {
        super(CURRENT_VERSION);
        this.subscribedEvents = subscribedEvents;

        if (subscribedEvents.size() > MAX_IDENTIFIERS) {
            throw new IllegalArgumentException("wantedPackets cannot contain more than " + MAX_IDENTIFIERS + " identifiers");
        }
    }

    public ServerboundRegisterPacket(PacketSerializer serializer) {
        super(serializer);
    }

    @Override
    protected boolean read(PacketSerializer serializer) {
        super.read(serializer);

        int size = serializer.readVarInt();
        if (size > MAX_IDENTIFIERS) {
            throw new IllegalArgumentException("wantedPackets cannot contain more than " + MAX_IDENTIFIERS + " identifiers");
        }

        this.subscribedEvents = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            subscribedEvents.put(serializer.readString(MAX_IDENTIFIER_LENGTH), serializer.readVarInt());
        }

        return true;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);

        serializer.writeVarInt(subscribedEvents.size());
        for (Map.Entry<String, Integer> entry : subscribedEvents.entrySet()) {
            serializer.writeString(entry.getKey(), MAX_IDENTIFIER_LENGTH);
            serializer.writeVarInt(entry.getValue());
        }
    }

    public Map<String, Integer> getSubscribedEvents() {
        return Collections.unmodifiableMap(subscribedEvents);
    }

    @Override
    public String toString() {
        return "ServerboundRegisterPacket{" +
                "wantedPackets=" + subscribedEvents +
                "} " + super.toString();
    }
}
