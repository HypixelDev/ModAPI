package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class PacketRegistry {

    private final Map<String, RegisteredType> registrations = new ConcurrentHashMap<>();
    private final Map<Class<? extends HypixelPacket>, String> classToIdentifier = new ConcurrentHashMap<>();

    public void registerPacketType(String identifier,
                                   Class<? extends ClientboundHypixelPacket> clientboundClazz, Function<PacketSerializer, ClientboundHypixelPacket> clientPacketFactory,
                                   Class<? extends HypixelPacket> serverboundClazz, Function<PacketSerializer, HypixelPacket> serverPacketFactory) {
        registrations.put(identifier, new RegisteredType(clientboundClazz, clientPacketFactory, serverboundClazz, serverPacketFactory));
        classToIdentifier.put(clientboundClazz, identifier);
        classToIdentifier.put(serverboundClazz, identifier);
    }

    private RegisteredType getRegisteredType(String identifier) {
        RegisteredType registeredType = registrations.get(identifier);
        if (registeredType == null) {
            throw new IllegalArgumentException("Unknown packet identifier: " + identifier);
        }
        return registeredType;
    }

    public boolean isRegistered(String identifier) {
        return registrations.containsKey(identifier);
    }

    public ClientboundHypixelPacket createClientboundPacket(String identifier, PacketSerializer serializer) {
        return getRegisteredType(identifier).clientPacketFactory.apply(serializer);
    }

    public HypixelPacket createServerboundPacket(String identifier, PacketSerializer serializer) {
        return getRegisteredType(identifier).serverPacketFactory.apply(serializer);
    }

    public String getIdentifier(Class<? extends HypixelPacket> clazz) {
        return classToIdentifier.get(clazz);
    }

    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(registrations.keySet());
    }

    private static final class RegisteredType {

        private final Class<? extends ClientboundHypixelPacket> clientboundClazz;
        private final Function<PacketSerializer, ClientboundHypixelPacket> clientPacketFactory;
        private final Class<? extends HypixelPacket> serverboundClazz;
        private final Function<PacketSerializer, HypixelPacket> serverPacketFactory;

        public RegisteredType(Class<? extends ClientboundHypixelPacket> clientboundClazz, Function<PacketSerializer, ClientboundHypixelPacket> clientPacketFactory,
                              Class<? extends HypixelPacket> serverboundClazz, Function<PacketSerializer, HypixelPacket> serverPacketFactory) {
            this.clientboundClazz = clientboundClazz;
            this.clientPacketFactory = clientPacketFactory;
            this.serverboundClazz = serverboundClazz;
            this.serverPacketFactory = serverPacketFactory;
        }
    }

}
