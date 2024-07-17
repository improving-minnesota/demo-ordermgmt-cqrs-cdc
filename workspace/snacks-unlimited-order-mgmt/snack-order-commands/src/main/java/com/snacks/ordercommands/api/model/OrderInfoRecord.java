package com.snacks.ordercommands.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderInfoRecord(
    String orderId,
    List<ItemDetailRecord> items) {

    @JsonProperty("totalPrice")
    public double totalPrice() {
        return items().stream().mapToDouble(item -> item.price() * item.quantity()).sum();
    }
}