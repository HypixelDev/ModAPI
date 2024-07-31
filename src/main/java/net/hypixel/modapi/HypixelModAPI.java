package net.hypixel.modapi;

import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.handler.ErrorHandler;
import net.hypixel.modapi.handler.RegisteredHandler;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.EventPacket;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.PacketRegistry;
import net.hypixel.modapi.packet.impl.clientbound.*;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPlayerInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundRegisterPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class HypixelModAPI {
    private static final HypixelModAPI INSTANCE = new HypixelModAPI();

    public static HypixelModAPI getInstance() {
        return INSTANCE;
    }

    private final PacketRegistry registry = new PacketRegistry();
    private final Map<String, Collection<RegisteredHandlerImpl<?>>> handlers = new ConcurrentHashMap<>();
    private final Set<String> subscribedEvents = ConcurrentHashMap.newKeySet();
    private Set<String> lastSubscribedEvents = Collections.emptySet();
    private Predicate<HypixelPacket> packetSender = null;

    private HypixelModAPI() {
        registerHypixelPackets();
        registerEventPackets();
        registerDefaultHandler();
    }

    private void registerHypixelPackets() {
        registry.define("hypixel:hello")
                .clientbound(ClientboundHelloPacket.class, ClientboundHelloPacket::new)
                .register();

        registry.define("hypixel:ping")
                .clientbound(ClientboundPingPacket.class, ClientboundPingPacket::new)
                .serverbound(ServerboundPingPacket.class, ServerboundPingPacket::new)
                .register();

        registry.define("hypixel:party_info")
                .clientbound(ClientboundPartyInfoPacket.class, ClientboundPartyInfoPacket::new)
                .serverbound(ServerboundPartyInfoPacket.class, ServerboundPartyInfoPacket::new)
                .register();

        registry.define("hypixel:player_info")
                .clientbound(ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacket::new)
                .serverbound(ServerboundPlayerInfoPacket.class, ServerboundPlayerInfoPacket::new)
                .register();

        registry.define("hypixel:register")
                .serverbound(ServerboundRegisterPacket.class, ServerboundRegisterPacket::new)
                .register();
    }

    private void registerEventPackets() {
        registry.define("hyevent:location")
                .clientBoundEvent(ClientboundLocationPacket.CURRENT_VERSION, ClientboundLocationPacket.class, ClientboundLocationPacket::new)
                .register();
    }

    private void registerDefaultHandler() {
        registerHandler(ClientboundHelloPacket.class, p -> sendRegisterPacket(true));
    }

    private void sendRegisterPacket(boolean alwaysSendIfNotEmpty) {
        if (packetSender == null) {
            // Allow registering events before the mod has fully initialized
            return;
        }

        if (lastSubscribedEvents.equals(subscribedEvents) && !(alwaysSendIfNotEmpty && !subscribedEvents.isEmpty())) {
            return;
        }

        Set<String> lastSubscribedEvents = new HashSet<>(subscribedEvents);
        if (sendPacket(new ServerboundRegisterPacket(registry, lastSubscribedEvents))) {
            this.lastSubscribedEvents = lastSubscribedEvents;
        }
    }

    @ApiStatus.Internal
    public PacketRegistry getRegistry() {
        return registry;
    }

    @ApiStatus.Internal
    public void handle(String identifier, PacketSerializer serializer) {
        if (handlers.isEmpty()) {
            return;
        }

        if (!registry.isRegistered(identifier)) {
            return;
        }

        // All responses contain a boolean of if the response is a success, if not then a further var int is included to identify the error
        if (!serializer.readBoolean()) {
            ErrorReason reason = ErrorReason.getById(serializer.readVarInt());
            handleError(identifier, reason);
            return;
        }

        ClientboundHypixelPacket packet = registry.createClientboundPacket(identifier, serializer);
        if (packet instanceof ClientboundVersionedPacket && !((ClientboundVersionedPacket) packet).isExpectedVersion()) {
            // Ignore packets that don't match our expected version, these could be received due to other mods requesting them
            return;
        }

        handle(packet);
    }

    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public void handle(ClientboundHypixelPacket packet) {
        Collection<RegisteredHandlerImpl<?>> typedHandlers = handlers.get(packet.getIdentifier());
        // nothing registered for this packet.
        if (typedHandlers == null) return;
        for (RegisteredHandlerImpl<?> handler : typedHandlers) {
            handler.handle(packet);
        }
    }

    @ApiStatus.Internal
    public void handleError(String identifier, ErrorReason reason) {
        Collection<RegisteredHandlerImpl<?>> handlers = this.handlers.get(identifier);
        if (handlers == null) return;
        for (RegisteredHandlerImpl<?> handler : handlers) {
            handler.handleError(reason);
        }
    }

    @ApiStatus.Internal
    public void setPacketSender(Predicate<HypixelPacket> packetSender) {
        if (this.packetSender != null) {
            throw new IllegalArgumentException("Packet sender already set");
        }
        this.packetSender = packetSender;
    }

    public <T extends ClientboundHypixelPacket> RegisteredHandler<T> registerHandler(Class<T> packetClass, ClientboundPacketHandler<T> handler) {
        if (packetClass == null) {
            throw new NullPointerException("packetClass cannot be null");
        }

        if (handler == null) {
            throw new NullPointerException("handler cannot be null");
        }

        RegisteredHandlerImpl<T> registeredHandler = new RegisteredHandlerImpl<>(handler);
        handlers.computeIfAbsent(getRegistry().getIdentifier(packetClass), cls -> new CopyOnWriteArrayList<>())
                .add(registeredHandler);
        return registeredHandler;
    }

    public void subscribeToEventPacket(Class<? extends EventPacket> packet) {
        if (subscribedEvents.add(getRegistry().getIdentifier(packet))) {
            sendRegisterPacket(false);
        }
    }

    /**
     * @return whether the packet was sent successfully
     */
    public boolean sendPacket(HypixelPacket packet) {
        if (packetSender == null) {
            throw new IllegalStateException("Packet sender not set");
        }

        return packetSender.test(packet);
    }

    private static class RegisteredHandlerImpl<T extends ClientboundHypixelPacket> implements RegisteredHandler<T> {
        private final ClientboundPacketHandler<T> handler;
        private ErrorHandler<T> errorHandler;

        RegisteredHandlerImpl(ClientboundPacketHandler<T> handler) {
            this.handler = handler;
        }

        @Override
        public void onError(ErrorHandler<T> errorHandler) {
            if (this.errorHandler != null) {
                throw new IllegalStateException("Error handler already set");
            }
            this.errorHandler = errorHandler;
        }

        void handle(ClientboundHypixelPacket packet) {
            // this cast is safe as we ensure its type when it is added to the handlers list in the first place.
            handler.handle((T) packet);
        }

        void handleError(ErrorReason reason) {
            if (errorHandler != null) {
                errorHandler.onError(reason);
            }
        }
    }
}
