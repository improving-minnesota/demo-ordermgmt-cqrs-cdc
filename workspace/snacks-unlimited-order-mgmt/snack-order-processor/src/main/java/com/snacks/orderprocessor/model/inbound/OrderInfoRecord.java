package com.snacks.orderprocessor.model.inbound;

import java.util.List;

public record OrderInfoRecord(
        String orderId,
        List<ItemDetailRecord> items) {

    public double totalPrice() {
        return items().stream().mapToDouble(item -> item.price() * item.quantity()).sum();
    }
}
