package com.snacks.orderprocessor.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snacks.orderprocessor.model.domain.value.ItemStatus;
import com.snacks.orderprocessor.model.domain.value.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerOrder {

    private String orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime modifiedDate;

    private List<ItemDetail> orderItems;
    private ShippingLocation shippingLocation;
    private Payment payment;

    public CustomerOrder() {
    }

    public CustomerOrder(String orderId, ShippingLocation shippingLocation, List<ItemDetail> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.shippingLocation = shippingLocation;
    }

    public CustomerOrder(String orderId, ShippingLocation shippingLocation, Payment payment, List<ItemDetail> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.shippingLocation = shippingLocation;
        this.payment = payment;
    }

    // region Setters for dates

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    // endregion

    // region Derived Getters
    public OrderStatus getOrderStatus() {
        OrderStatus status = OrderStatus.PENDING;
        if (payment != null) {
            status = OrderStatus.PAID;
        }

        // Based on Item Status the order can be fulfilled or shipped
         if (allItemsHaveStatus(ItemStatus.FULFILLED)) {
             status = OrderStatus.FULFILLED;
         } else if (allItemsHaveStatus(ItemStatus.SHIPPED)) {
             status = OrderStatus.SHIPPED;
         }

        return status;
    }
    // endregion

    // region Getters
    public String getOrderId() {
        return orderId;
    }

    public List<ItemDetail> getItems() {
        return orderItems;
    }

    public ShippingLocation getShippingLocation() {
        return shippingLocation;
    }

    public Payment getPayment() {
        return payment;
    }
    // endregion

    // region Private Helpers
    private boolean allItemsHaveStatus(ItemStatus status) {
        for (ItemDetail item : orderItems) {
            if (item.getItemStatus() != status) {
                return false;
            }
        }
        return true;
    }
    // endregion
}
