package com.bos.config.billing.exceptions;

public class OrderNotFoundException extends BillingException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}