package com.bos.config.order.exceptions;

public class CatalogItemNotFoundException extends OrderException {
    public CatalogItemNotFoundException(String message) {
        super(message);
    }

    public CatalogItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}