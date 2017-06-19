package juja.microservices.keepers.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KeeperDirectionRequestTest {

    @Test
    public void shouldReturnDirection() {
        String expectedDirection = "foo-bar";

        KeeperDirectionRequest request = new KeeperDirectionRequest("0000-0000");

        assertNotNull(request);
        assertEquals(expectedDirection, request.getDirection());
    }
}
