package com.fulfilment.application.monolith.exception;

public class LocationNotFound extends RuntimeException {

    public LocationNotFound() {
    }

    public LocationNotFound(String message) {
        super(message);
    }
}
