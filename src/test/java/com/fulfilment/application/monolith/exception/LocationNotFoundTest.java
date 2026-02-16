package com.fulfilment.application.monolith.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationNotFoundTest {

    @Test
    void shouldCreateExceptionWithNoArgs() {
        LocationNotFound exception = new LocationNotFound();

        assertNotNull(exception);
        assertInstanceOf(RuntimeException.class, exception);
        assertNull(exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Location not found";

        LocationNotFound exception = new LocationNotFound(message);

        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
