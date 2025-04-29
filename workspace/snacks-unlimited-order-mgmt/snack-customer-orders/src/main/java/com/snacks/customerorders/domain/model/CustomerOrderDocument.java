package com.snacks.customerorders.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "customerOrder")
public class CustomerOrderDocument {
    @Id
    private String orderId;

    private CustomerOrder customerOrder;

    // region Getters and setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    // endregion

    // region Getter extractors for shorter Spring JPA method names
    public String getOrderStatus() {
        return customerOrder != null ? customerOrder.getOrderStatus() : null;
    }

    public LocalDateTime getModifiedDate() {
        return customerOrder != null ? customerOrder.getModifiedDate() : null;
    }

    public List<ItemDetail> getItems() {
        return customerOrder != null ? customerOrder.getItems() : null;
    }

    public String getCreditCardType() {
        return customerOrder != null ? customerOrder.getPayment().getCreditCardType() : null;
    }
    // endregion
}
