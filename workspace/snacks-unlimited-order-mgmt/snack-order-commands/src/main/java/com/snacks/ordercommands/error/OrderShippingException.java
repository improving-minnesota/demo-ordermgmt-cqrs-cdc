package com.snacks.ordercommands.error;

public class OrderShippingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderShippingException(String message) {
        super(message);
    }
}
