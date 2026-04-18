package com.bos.config.hospitality.kitchen.exceptions;

public class InvalidKitchenStatusException extends RuntimeException {
    public InvalidKitchenStatusException(String message) {
        super(message);
    }

    public InvalidKitchenStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}