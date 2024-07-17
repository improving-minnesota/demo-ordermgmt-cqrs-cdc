package com.snacks.ordercommands.domain.model.entity;

import com.snacks.ordercommands.domain.model.value.ItemStatus;
import com.snacks.ordercommands.domain.model.value.Price;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "ITEM_DETAIL")
@IdClass(OrderItemId.class)
public class ItemDetail {

    @Id
    private String orderId;

    @Id
    private String itemId;

    private String itemName;
    private ItemStatus itemStatus;

    @Embedded
    private Price price;

    private Integer quantity;

    public ItemDetail() {
    }

    public ItemDetail(String orderId, String itemId, String itemName, ItemStatus itemStatus, Price price, Integer quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.itemStatus = itemStatus;
    }

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

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
