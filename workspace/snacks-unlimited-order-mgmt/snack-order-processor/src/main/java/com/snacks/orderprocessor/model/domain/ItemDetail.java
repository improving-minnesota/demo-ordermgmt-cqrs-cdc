package com.snacks.orderprocessor.model.domain;

import com.snacks.orderprocessor.model.domain.value.ItemStatus;
import com.snacks.orderprocessor.model.domain.value.Price;

public class ItemDetail {

    private String itemId;
    private String itemName;
    private ItemStatus itemStatus;
    private Price price;
    private Integer quantity;

    public ItemDetail() {
    }

    public ItemDetail(String itemId, String itemName, ItemStatus itemStatus, Price price, Integer quantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemStatus = itemStatus;
        this.price = price;
        this.quantity = quantity;
    }

    // region Getters

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
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

    // endregion
}
