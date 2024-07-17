package com.snacks.ordercommands.api.model;

public record ItemStatusUpdateRecord(
    String orderId,
    String itemStatus) {
}