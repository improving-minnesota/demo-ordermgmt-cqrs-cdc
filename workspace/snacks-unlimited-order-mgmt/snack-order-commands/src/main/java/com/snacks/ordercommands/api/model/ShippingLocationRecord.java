package com.snacks.ordercommands.api.model;

public record ShippingLocationRecord(
    String orderId,
    String customerName,
    String customerAddress,
    String zipCode,
    Double latitude, Double longitude) {
}
