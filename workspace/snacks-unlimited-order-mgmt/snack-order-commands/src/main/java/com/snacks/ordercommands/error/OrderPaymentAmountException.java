package com.snacks.ordercommands.error;

public class OrderPaymentAmountException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderPaymentAmountException(String message) {
        super(message);
    }
}
