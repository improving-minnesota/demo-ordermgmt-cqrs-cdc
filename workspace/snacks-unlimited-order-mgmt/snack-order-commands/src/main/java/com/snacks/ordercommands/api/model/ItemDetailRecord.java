package com.snacks.ordercommands.api.model;

public record ItemDetailRecord(
    String itemId,
    String itemName,
    String itemStatus,
    Double price,
    Integer quantity) {
}