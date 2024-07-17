package com.snacks.customerorders.domain.model;

import java.util.List;

public class CustomerOrder {

    private String orderId;

    private String orderStatus;

    private ShippingLocation shippingLocation;

    private Payment payment;

    private List<ItemDetail> items;

    // region Getters and Setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ShippingLocation getShippingLocation() {
        return shippingLocation;
    }

    public void setShippingLocation(ShippingLocation shippingLocation) {
        this.shippingLocation = shippingLocation;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<ItemDetail> getItems() {
        return items;
    }

    public void setItems(List<ItemDetail> items) {
        this.items = items;
    }

    // endregion
}
