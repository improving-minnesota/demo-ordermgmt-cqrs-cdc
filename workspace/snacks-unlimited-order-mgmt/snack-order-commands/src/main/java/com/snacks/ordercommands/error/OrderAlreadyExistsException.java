package com.snacks.ordercommands.error;

public class OrderAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderAlreadyExistsException(String message) {
        super(message);
    }
}
