package com.snacks.orderprocessor.model.inbound;

public record ItemDetailRecord(
    String itemId,
    String itemName,
    String itemStatus,
    Double price,
    Integer quantity) {
}