package com.snacks.orderprocessor.model.domain;

import com.snacks.orderprocessor.model.domain.value.Amount;

public class Payment {

    private String paymentType;
    private String creditCardType;
    private String creditCardNumber;

    private Amount amount;

    public Payment() {
    }

    public Payment(String paymentType, String creditCardType, String creditCardNumber, Amount amount) {
        this.paymentType = paymentType;
        this.creditCardType = creditCardType;
        this.creditCardNumber = creditCardNumber;
        this.amount = amount;
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
