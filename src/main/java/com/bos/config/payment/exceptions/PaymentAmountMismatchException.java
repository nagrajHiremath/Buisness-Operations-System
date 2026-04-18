package com.bos.config.payment.exceptions;

public class PaymentAmountMismatchException extends PaymentException {
    public PaymentAmountMismatchException(String message) {
        super(message);
    }
}