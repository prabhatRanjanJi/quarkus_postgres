package com.fulfilment.application.monolith.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvalidInputExceptionTest {

    @Test
    void shouldCreateExceptionWithNoArgs() {
        InvalidInputException exception = new InvalidInputException();

        assertNotNull(exception);
        assertInstanceOf(RuntimeException.class, exception);
        assertNull(exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Invalid input provided";

        InvalidInputException exception = new InvalidInputException(message);

        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
