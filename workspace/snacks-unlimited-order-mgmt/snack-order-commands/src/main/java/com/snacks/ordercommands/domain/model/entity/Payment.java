package com.snacks.ordercommands.domain.model.entity;

import com.snacks.ordercommands.domain.model.value.Amount;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PAYMENT")
public class Payment {

    @Id
    private String orderId;
    private String paymentType;
    private String creditCardType;
    private String creditCardNumber;

    @Embedded
    Amount amount;

    public Payment() {
    }

    public Payment(String orderId, String paymentType, String creditCardType, String creditCardNumber, Amount amount) {
        this.orderId = orderId;
        this.paymentType = paymentType;
        this.creditCardType = creditCardType;
        this.creditCardNumber = creditCardNumber;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
