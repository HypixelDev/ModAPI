package net.hypixe.modapi;

import net.hypixel.modapi.HypixelModAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPacketIdentifierLength {
    private static final int LIMIT = 20;

    @Test
    void testPacketIdentifierLength() {
        for (String identifier : HypixelModAPI.getInstance().getRegistry().getIdentifiers()) {
            Assertions.assertTrue(identifier.length() <= LIMIT, String.format("Identifier %s is too long (length %d)", identifier, identifier.length()));
        }
    }

}
