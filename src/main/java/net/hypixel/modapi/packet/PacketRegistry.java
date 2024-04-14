package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class PacketRegistry {

    private final Map<String, Registration> registrations = new ConcurrentHashMap<>();
    private final Map<Class<? extends HypixelPacket>, String> classToIdentifier = new ConcurrentHashMap<>();

    private void register(
            String identifier,
            Class<? extends ClientboundHypixelPacket> clientboundClazz, Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory,
            Class<? extends HypixelPacket> serverboundClazz, Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory) {
        registrations.put(identifier, new Registration(clientboundClazz, clientPacketFactory, serverboundClazz, serverPacketFactory));
        classToIdentifier.put(clientboundClazz, identifier);
        classToIdentifier.put(serverboundClazz, identifier);
    }

    public RegistrationBuilder define(String identifier) {
        return new RegistrationBuilder(this, identifier);
    }

    private Registration getRegistration(String identifier) {
        Registration registration = registrations.get(identifier);
        if (registration == null) {
            throw new IllegalArgumentException("Unknown packet identifier: " + identifier);
        }
        return registration;
    }

    public boolean isRegistered(String identifier) {
        return registrations.containsKey(identifier);
    }

    public ClientboundHypixelPacket createClientboundPacket(String identifier, PacketSerializer serializer) {
        return getRegistration(identifier).clientPacketFactory.apply(serializer);
    }

    public HypixelPacket createServerboundPacket(String identifier, PacketSerializer serializer) {
        return getRegistration(identifier).serverPacketFactory.apply(serializer);
    }

    public String getIdentifier(Class<? extends HypixelPacket> clazz) {
        return classToIdentifier.get(clazz);
    }

    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(registrations.keySet());
    }

    public static final class RegistrationBuilder {
        private final PacketRegistry registry;
        private final String identifier;

        private Class<? extends ClientboundHypixelPacket> clientboundClazz;
        private Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory;
        private Class<? extends HypixelPacket> serverboundClazz;
        private Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory;

        RegistrationBuilder(PacketRegistry registry, String identifier) {
            this.registry = registry;
            this.identifier = identifier;
        }

        public <T extends ClientboundHypixelPacket> RegistrationBuilder clientbound(
                Class<T> clientboundClazz, Function<PacketSerializer, T> clientPacketFactory) {
            this.clientboundClazz = clientboundClazz;
            this.clientPacketFactory = clientPacketFactory;
            return this;
        }

        public <T extends HypixelPacket> RegistrationBuilder serverbound(
                Class<T> serverboundClazz, Function<PacketSerializer, T> serverPacketFactory) {
            this.serverboundClazz = serverboundClazz;
            this.serverPacketFactory = serverPacketFactory;
            return this;
        }

        public void register() {
            registry.register(identifier, clientboundClazz, clientPacketFactory, serverboundClazz, serverPacketFactory);
        }

    }

    private static final class Registration {

        private final Class<? extends ClientboundHypixelPacket> clientboundClazz;
        private final Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory;
        private final Class<? extends HypixelPacket> serverboundClazz;
        private final Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory;

        public Registration(Class<? extends ClientboundHypixelPacket> clientboundClazz, Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory,
                            Class<? extends HypixelPacket> serverboundClazz, Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory) {
            this.clientboundClazz = clientboundClazz;
            this.clientPacketFactory = clientPacketFactory;
            this.serverboundClazz = serverboundClazz;
            this.serverPacketFactory = serverPacketFactory;
        }

        @Override
        public String toString() {
            return "Registration{" +
                    "clientboundClazz=" + clientboundClazz +
                    ", clientPacketFactory=" + clientPacketFactory +
                    ", serverboundClazz=" + serverboundClazz +
                    ", serverPacketFactory=" + serverPacketFactory +
                    '}';
        }
    }

}
