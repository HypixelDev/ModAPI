package net.hypixel.modapi.packet.impl.clientbound;

import net.hypixel.data.region.Environment;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;

/**
 * This packet is automatically sent on every join to Hypixel to indicate that the client has connected to a Hypixel server.
 * Due to the nature of this packet, it is implemented without versioning and designed to discard extra bytes in the case that more data is added in the future.
 */
public class ClientboundHelloPacket implements ClientboundHypixelPacket {
    private final Environment environment;

    public ClientboundHelloPacket(Environment environment) {
        this.environment = environment;
    }

    public ClientboundHelloPacket(PacketSerializer serializer) {
        this.environment = Environment.getById(serializer.readVarInt()).orElseThrow(() -> new IllegalArgumentException("Invalid environment ID"));

        // Read any remaining bytes, so that if more data is added in the future, it will be discarded on older clients
        serializer.discardRemaining();
    }

    @Override
    public void write(PacketSerializer serializer) {
        serializer.writeVarInt(environment.getId());
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public String toString() {
        return "ClientboundHelloPacket{" +
                "environment=" + environment +
                '}';
    }

}
