package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.Store;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreOperationEventTest {

    @Test
    void shouldCreateEventWithStoreAndEventType() {
        // Arrange
        Store store = new Store();
        String eventType = "CREATE";

        // Act
        StoreOperationEvent event = new StoreOperationEvent(store, eventType);

        // Assert
        assertNotNull(event);
        assertEquals(store, event.getStore());
        assertEquals(eventType, event.getEvent());
    }

    @Test
    void shouldAllowNullStore() {
        // Act
        StoreOperationEvent event = new StoreOperationEvent(null, "UPDATE");

        // Assert
        assertNull(event.getStore());
        assertEquals("UPDATE", event.getEvent());
    }

    @Test
    void shouldAllowNullEventType() {
        // Arrange
        Store store = new Store();

        // Act
        StoreOperationEvent event = new StoreOperationEvent(store, null);

        // Assert
        assertEquals(store, event.getStore());
        assertNull(event.getEvent());
    }
}
