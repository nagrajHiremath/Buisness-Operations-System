package com.bos.config.billing.exceptions;

public class InvoiceAlreadyExistsException extends BillingException {
    public InvoiceAlreadyExistsException(String message) {
        super(message);
    }
}