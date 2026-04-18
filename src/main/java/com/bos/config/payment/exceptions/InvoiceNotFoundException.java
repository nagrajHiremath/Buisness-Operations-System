package com.bos.config.payment.exceptions;

public class InvoiceNotFoundException extends PaymentException {
    public InvoiceNotFoundException(String message) {
        super(message);
    }
}