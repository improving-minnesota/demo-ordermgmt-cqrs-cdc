package com.snacks.ordercommands.utils;

import com.snacks.ordercommands.api.model.PaymentRecord;
import com.snacks.ordercommands.domain.model.entity.Payment;
import com.snacks.ordercommands.domain.model.value.Amount;
import com.snacks.ordercommands.domain.model.value.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PaymentUtils {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentUtils.class);

    // region converters
    public static Payment convertToEntity(PaymentRecord paymentRecord) {
        Payment payment = new Payment();

        payment.setOrderId(paymentRecord.orderId());
        payment.setPaymentType(paymentRecord.paymentType());
        payment.setCreditCardType(paymentRecord.creditCardType());
        payment.setCreditCardNumber(paymentRecord.creditCardNumber());
        payment.setAmount(new Amount(paymentRecord.amount(), Currency.USD));

        return payment;
    }

    public static PaymentRecord convertToRecord(Payment payment) {
        PaymentRecord paymentRecord = new PaymentRecord(
                payment.getOrderId(),
                payment.getPaymentType(),
                payment.getCreditCardType(),
                payment.getCreditCardNumber(),
                payment.getAmount().amount());

        return paymentRecord;
    }
    // endregion


}
