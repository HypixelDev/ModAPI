package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.annotation.Experimental;
import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.HashMap;
import java.util.Map;

@Experimental
public class ServerboundRegisterPacket extends ServerboundVersionedPacket {
    private static final int MAX_IDENTIFIER_LENGTH = 20;
    private static final int MAX_IDENTIFIERS = 5;
    private static final int CURRENT_VERSION = 1;

    private Map<String, Integer> wantedPackets;

    public ServerboundRegisterPacket(Map<String, Integer> wantedPackets) {
        super(CURRENT_VERSION);
        this.wantedPackets = wantedPackets;

        if (wantedPackets.isEmpty()) {
            throw new IllegalArgumentException("wantedPackets cannot be empty");
        }

        if (wantedPackets.size() > MAX_IDENTIFIERS) {
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

        this.wantedPackets = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            wantedPackets.put(serializer.readString(MAX_IDENTIFIER_LENGTH), serializer.readVarInt());
        }

        return true;
    }

    @Override
    public void write(PacketSerializer serializer) {
        super.write(serializer);

        serializer.writeVarInt(wantedPackets.size());
        for (Map.Entry<String, Integer> entry : wantedPackets.entrySet()) {
            serializer.writeString(entry.getKey(), MAX_IDENTIFIER_LENGTH);
            serializer.writeVarInt(entry.getValue());
        }
    }
}
