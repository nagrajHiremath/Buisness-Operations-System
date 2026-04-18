package com.bos.config.billing.exceptions;

public class InvalidOrderStatusException extends BillingException {
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}