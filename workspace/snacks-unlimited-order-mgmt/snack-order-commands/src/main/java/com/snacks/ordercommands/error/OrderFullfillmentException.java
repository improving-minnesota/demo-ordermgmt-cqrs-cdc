package com.snacks.ordercommands.error;

public class OrderFullfillmentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderFullfillmentException(String message) {
        super(message);
    }
}
