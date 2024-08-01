package net.hypixel.modapi;

import net.hypixel.modapi.error.BuiltinErrorReason;
import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.handler.ErrorHandler;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundRegisterPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundRegisterPacket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Deals with all the logic surrounding event subscription.
 * <br>
 * Some things to note:
 * <ul>
 *     <li>The last sent set of events are always the state stored on the server, so the whole events list has to be sent.</li>
 *     <li>The server has a small cooldown between sending registration packets, this can result in receiving a RATE_LIMITED response if this cooldown is hit.</li>
 *     <li>Because of the above, we attempt to batch subscriptions, so when we first get a subscribe call we wait before sending the packet in-case more follow.</li>
 *     <li>If we are ever RATE_LIMITED, we will attempt to send the registration again shortly after.</li>
 * </ul>
 */
class EventSubscriptionHandler implements ClientboundPacketHandler<ClientboundRegisterPacket>, ErrorHandler {
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "HypixelEventSubscriptionHandler");
        thread.setDaemon(true);
        return thread;
    });
    private static final long DEFAULT_RESEND_DELAY_MS = 5_000L;

    private final HypixelModAPI api;

    // Indicates we are pending to send the registration shortly
    private final AtomicBoolean sendingScheduled = new AtomicBoolean(false);

    // Indicates we are waiting for a response to the registration, and we should not send another until we get one (or timeout)
    private final AtomicBoolean pendingResponse = new AtomicBoolean(false);
    // The current packet identifier, used to match responses to requests
    private final AtomicInteger packetIdentifier = new AtomicInteger(0);

    private final Set<String> subscribedEvents = ConcurrentHashMap.newKeySet();
    private Set<String> lastSubscribedEvents = Collections.emptySet();

    EventSubscriptionHandler(HypixelModAPI api) {
        this.api = api;
    }

    void subscribeToEventPacket(String identifier) {
        if (subscribedEvents.add(identifier)) {
            sendRegisterPacket(false);
        }
    }

    void sendRegisterPacket(boolean alwaysSendIfNotEmpty) {
        if (!api.isPacketSenderSet()) {
            // Allow registering events before the mod has fully initialized
            return;
        }

        if (lastSubscribedEvents.equals(subscribedEvents) && !(alwaysSendIfNotEmpty && !subscribedEvents.isEmpty())) {
            return;
        }

        if (!sendingScheduled.compareAndSet(false, true)) {
            return;
        }

        // We wait 500ms before sending the packet, this gives further subscribe calls a chance to batch (such as mods registering them during server-join)
        SCHEDULER.schedule(() -> {
            sendingScheduled.set(false);
            actuallySendRegisterPacket();
        }, 500, TimeUnit.MILLISECONDS);
    }

    void actuallySendRegisterPacket() {
        if (!pendingResponse.compareAndSet(false, true)) {
            // We are already waiting for a response, so we should instead schedule to update it again later
            scheduleResend(DEFAULT_RESEND_DELAY_MS);
            return;
        }

        Set<String> lastSubscribedEvents = new HashSet<>(subscribedEvents);
        int requestIdentifier = packetIdentifier.incrementAndGet();

        if (api.sendPacket(new ServerboundRegisterPacket(api.getRegistry(), requestIdentifier, lastSubscribedEvents))) {
            this.lastSubscribedEvents = lastSubscribedEvents;

            // Schedule a 5s timeout for getting a response on the registration
            SCHEDULER.schedule(() -> {
                if (pendingResponse.compareAndSet(true, false)) {
                    // If we timed out, try again in 5s
                    scheduleResend(DEFAULT_RESEND_DELAY_MS);
                }
            }, 5, TimeUnit.SECONDS);
        } else {
            // If we failed to send the packet, unset the pending response flag
            pendingResponse.set(false);
        }
    }

    @Override
    public void handle(ClientboundRegisterPacket packet) {
        int requestIdentifier = packet.getRequestIdentifier();
        if (requestIdentifier != packetIdentifier.get()) {
            throw new IllegalStateException("Received a response for an unknown request identifier: " + requestIdentifier);
        }

        pendingResponse.set(false);
    }

    @Override
    public void onError(ErrorReason reason) {
        pendingResponse.set(false);

        if (reason != BuiltinErrorReason.RATE_LIMITED) {
            throw new IllegalStateException("Failed to send register packet: " + reason);
        }

        scheduleResend(DEFAULT_RESEND_DELAY_MS);
    }

    private void scheduleResend(long delayMs) {
        SCHEDULER.schedule(this::actuallySendRegisterPacket, delayMs, TimeUnit.MILLISECONDS);
    }
}
