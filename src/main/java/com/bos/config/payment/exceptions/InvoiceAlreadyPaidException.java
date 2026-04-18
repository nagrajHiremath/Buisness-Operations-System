package com.bos.config.payment.exceptions;

public class InvoiceAlreadyPaidException extends PaymentException {
    public InvoiceAlreadyPaidException(String message) {
        super(message);
    }
}