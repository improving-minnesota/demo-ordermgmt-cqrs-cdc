package com.snacks.orderprocessor.model.inbound;

public record ShippingLocationRecord(
    String orderId,
    String customerName,
    String customerAddress,
    String zipCode,
    Double latitude,
    Double longitude) {
}