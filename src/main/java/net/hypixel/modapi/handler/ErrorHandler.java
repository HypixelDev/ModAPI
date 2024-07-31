package net.hypixel.modapi.handler;

import net.hypixel.modapi.error.ErrorReason;

@FunctionalInterface
public interface ErrorHandler {
    void onError(ErrorReason reason);
}
