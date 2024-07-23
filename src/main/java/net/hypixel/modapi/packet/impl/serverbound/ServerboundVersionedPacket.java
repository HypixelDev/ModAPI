package net.hypixel.modapi.packet.impl.serverbound;

import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.impl.VersionedPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

public abstract class ServerboundVersionedPacket extends VersionedPacket implements HypixelPacket {

    @ApiStatus.Internal
    public ServerboundVersionedPacket(int version) {
        super(version);
    }

    @ApiStatus.Internal
    public ServerboundVersionedPacket(PacketSerializer serializer) {
        super(serializer);
    }

    protected boolean read(PacketSerializer serializer) {
        this.version = serializer.readVarInt();
        return true; // Always continue reading serverbound, we should handle the packet based on the version
    }

    @Override
    public void write(PacketSerializer serializer) {
        serializer.writeVarInt(version);
    }

}
