package com.snacks.ordercommands.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class OrderItemId implements Serializable {

    private static final long serialVersionUID = 1L;

    public OrderItemId() {
    }

    public OrderItemId(String orderId, String itemId) {
        this.orderId = orderId;
        this.itemId = itemId;
    }

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "ITEM_ID")
    private String itemId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
