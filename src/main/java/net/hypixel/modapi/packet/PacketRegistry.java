package net.hypixel.modapi.packet;

import net.hypixel.modapi.serializer.PacketSerializer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PacketRegistry {

    private final Map<String, Registration> registrations = new ConcurrentHashMap<>();
    private final Map<Class<? extends HypixelPacket>, String> classToIdentifier = new ConcurrentHashMap<>();

    private void register(
            String identifier,
            int version,
            Class<? extends ClientboundHypixelPacket> clientboundClazz, Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory,
            Class<? extends HypixelPacket> serverboundClazz, Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory) {
        registrations.put(identifier, new Registration(version, clientboundClazz, clientPacketFactory, serverboundClazz, serverPacketFactory));
        if (clientboundClazz != null) {
            classToIdentifier.put(clientboundClazz, identifier);
        }
        if (serverboundClazz != null) {
            classToIdentifier.put(serverboundClazz, identifier);
        }
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

    Collection<Registration> getRegistrations() {
        return registrations.values();
    }

    public ClientboundHypixelPacket createClientboundPacket(String identifier, PacketSerializer serializer) {
        return getRegistration(identifier).clientPacketFactory.apply(serializer);
    }

    public HypixelPacket createServerboundPacket(String identifier, PacketSerializer serializer) {
        return getRegistration(identifier).serverPacketFactory.apply(serializer);
    }

    /**
     * @return whether a packet with the given identifier is registered, either for clientbound or serverbound
     */
    public boolean isRegistered(String identifier) {
        return registrations.containsKey(identifier);
    }

    public String getIdentifier(Class<? extends HypixelPacket> clazz) {
        return classToIdentifier.get(clazz);
    }

    /**
     * @return all registered packet identifiers, this will include both clientbound and serverbound packets
     */
    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(registrations.keySet());
    }

    /**
     * @return all registered clientbound packet identifiers
     */
    public Set<String> getClientboundIdentifiers() {
        return Collections.unmodifiableSet(registrations.values().stream()
                .filter(registration -> registration.getClientboundClazz() != null)
                .map(registration -> classToIdentifier.get(registration.getClientboundClazz()))
                .collect(Collectors.toSet()));
    }

    /**
     * @return all registered serverbound packet identifiers
     */
    public Set<String> getServerboundIdentifiers() {
        return Collections.unmodifiableSet(registrations.values().stream()
                .filter(registration -> registration.getServerboundClazz() != null)
                .map(registration -> classToIdentifier.get(registration.getServerboundClazz()))
                .collect(Collectors.toSet()));
    }

    /**
     * @param wantedEventIdentifiers the identifiers of the event packets we want to get versions for
     * @return a map of the event identifiers to their versions, only includes event packets that are registered as event packets with known versions
     */
    public Map<String, Integer> getEventVersions(Set<String> wantedEventIdentifiers) {
        return wantedEventIdentifiers.stream()
                .filter(this::isRegistered)
                .filter(identifier -> getRegistration(identifier).getVersion() > 0)
                .collect(Collectors.toMap(Function.identity(), identifier -> getRegistration(identifier).getVersion()));
    }

    public static final class RegistrationBuilder {
        private final PacketRegistry registry;
        private final String identifier;

        private Class<? extends ClientboundHypixelPacket> clientboundClazz;
        private Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory;
        private Class<? extends HypixelPacket> serverboundClazz;
        private Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory;
        private int version = 0;

        RegistrationBuilder(PacketRegistry registry, String identifier) {
            this.registry = registry;
            this.identifier = identifier;
        }

        public <T extends ClientboundHypixelPacket> RegistrationBuilder clientBoundEvent(
                int version, Class<T> clientboundClazz, Function<PacketSerializer, T> clientPacketFactory) {
            this.version = version;
            this.clientboundClazz = clientboundClazz;
            this.clientPacketFactory = clientPacketFactory;
            return this;
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
            registry.register(identifier, version, clientboundClazz, clientPacketFactory, serverboundClazz, serverPacketFactory);
        }

    }

    static final class Registration {

        private final int version;
        private final Class<? extends ClientboundHypixelPacket> clientboundClazz;
        private final Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory;
        private final Class<? extends HypixelPacket> serverboundClazz;
        private final Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory;

        public Registration(int version,
                            Class<? extends ClientboundHypixelPacket> clientboundClazz, Function<PacketSerializer, ? extends ClientboundHypixelPacket> clientPacketFactory,
                            Class<? extends HypixelPacket> serverboundClazz, Function<PacketSerializer, ? extends HypixelPacket> serverPacketFactory) {
            this.version = version;
            this.clientboundClazz = clientboundClazz;
            this.clientPacketFactory = clientPacketFactory;
            this.serverboundClazz = serverboundClazz;
            this.serverPacketFactory = serverPacketFactory;
        }

        int getVersion() {
            return version;
        }

        Class<? extends ClientboundHypixelPacket> getClientboundClazz() {
            return clientboundClazz;
        }

        Class<? extends HypixelPacket> getServerboundClazz() {
            return serverboundClazz;
        }

        @Override
        public String toString() {
            return "Registration{" +
                    "clientboundClazz=" + clientboundClazz +
                    ", serverboundClazz=" + serverboundClazz +
                    '}';
        }
    }

}
