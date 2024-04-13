package net.hypixel.modapi;

import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.handler.RegisteredHandler;

import java.util.List;

final class RegisteredHandlerImpl implements RegisteredHandler {
    private final List<ClientboundPacketHandler> handlers;
    private final ClientboundPacketHandler handler;
    private volatile boolean deregistered;

    RegisteredHandlerImpl(List<ClientboundPacketHandler> handlers, ClientboundPacketHandler handler) {
        this.handlers = handlers;
        this.handler = handler;
    }

    @Override
    public void deregister() {
        if (deregistered) return;
        synchronized (this) {
            if (deregistered) return;
            deregistered = true;
        }
        handlers.remove(handler);
    }
}
