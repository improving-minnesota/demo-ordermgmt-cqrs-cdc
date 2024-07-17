package com.snacks.ordercommands.domain.repository;

import com.snacks.ordercommands.domain.model.entity.Payment;
import com.snacks.ordercommands.domain.model.value.Amount;
import com.snacks.ordercommands.domain.model.value.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testSaveAndRetrieveShippingDetails() {
        // Given
        Payment payment = new Payment("123",
                "Credit Card", "Visa", "1111-2222-3333-4444",
                new Amount(70.00, Currency.USD));

        // When
        paymentRepository.save(payment);
        Payment retrievedPayment = paymentRepository.findById(payment.getOrderId()).orElse(null);

        // Then
        assertThat(retrievedPayment).isNotNull();
        assertThat(retrievedPayment.getOrderId()).isEqualTo(payment.getOrderId());
        assertThat(retrievedPayment.getPaymentType()).isEqualTo(payment.getPaymentType());
        assertThat(retrievedPayment.getCreditCardType()).isEqualTo(payment.getCreditCardType());
        assertThat(retrievedPayment.getCreditCardNumber()).isEqualTo(payment.getCreditCardNumber());
        assertThat(retrievedPayment.getAmount()).isEqualTo(payment.getAmount());
    }


}
