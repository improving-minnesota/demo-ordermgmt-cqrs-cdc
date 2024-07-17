package com.snacks.orderprocessor.model.inbound;

public record PaymentRecord(
    String orderId,
    String paymentType,
    String creditCardType,
    String creditCardNumber,
    Double amount) {
}