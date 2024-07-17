package com.snacks.ordercommands.api.model;

public record PaymentRecord(
    String orderId,
    String paymentType,
    String creditCardType,
    String creditCardNumber,
    Double amount) {
}