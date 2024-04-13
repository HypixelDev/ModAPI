package net.hypixe.modapi;

import net.hypixel.modapi.HypixelModAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPacketIdentifierLength {
    private static final int LIMIT = 20;

    private static final HypixelModAPI MOD_API = new HypixelModAPI();

    @Test
    void testPacketIdentifierLength() {

        for (String identifier : MOD_API.getRegistry().getIdentifiers()) {
            Assertions.assertTrue(identifier.length() <= LIMIT, String.format("Identifier %s is too long (length %d)", identifier, identifier.length()));
        }
    }

}
