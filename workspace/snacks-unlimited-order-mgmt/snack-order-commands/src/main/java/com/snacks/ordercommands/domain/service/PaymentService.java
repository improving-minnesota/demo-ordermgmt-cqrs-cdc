package com.snacks.ordercommands.domain.service;

import com.snacks.ordercommands.api.model.OrderInfoRecord;
import com.snacks.ordercommands.api.model.PaymentRecord;
import com.snacks.ordercommands.domain.model.entity.ItemDetail;
import com.snacks.ordercommands.domain.model.entity.Payment;
import com.snacks.ordercommands.domain.repository.ItemDetailRepository;
import com.snacks.ordercommands.domain.repository.PaymentRepository;
import com.snacks.ordercommands.error.OrderDoesNotExistException;
import com.snacks.ordercommands.error.OrderPaymentAmountException;
import com.snacks.ordercommands.utils.ItemDetailUtils;
import com.snacks.ordercommands.utils.PaymentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    ItemDetailRepository itemDetailRepository;
    PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(ItemDetailRepository itemDetailRepository, PaymentRepository paymentRepository) {
        this.itemDetailRepository = itemDetailRepository;
        this.paymentRepository = paymentRepository;
    }

    public PaymentRecord save(PaymentRecord paymentRecord) {
        log.info("Entering PaymentService.save() with paymentRecord :: {}", paymentRecord);

        validate(paymentRecord);

        Payment savedPayment = paymentRepository.save(PaymentUtils.convertToEntity(paymentRecord));

        log.info("Leaving ShippingDetailsService.save() with savedPayment :: {}", savedPayment);
        return PaymentUtils.convertToRecord(savedPayment);
    }

    // region Private Methods
    private void validate(PaymentRecord paymentRecord) {
        // Needs to be a valid order before setting Shipping Address
        if (ObjectUtils.isEmpty(paymentRecord.orderId())) {
            throw new OrderDoesNotExistException("Missing a valid Order Id for Payment.");
        }

        List<ItemDetail> itemDetailList = itemDetailRepository.findByOrderId(paymentRecord.orderId());
        
        if (itemDetailList.isEmpty()) {
            throw new OrderDoesNotExistException("Need a valid Order to make a payment. No Order found for Order Id: " + paymentRecord.orderId());
        }
        
        // Verify the total amount
        validatePaymentAmount(paymentRecord, itemDetailList);
    }

    private static void validatePaymentAmount(PaymentRecord paymentRecord, List<ItemDetail> itemDetailList) {
        OrderInfoRecord orderInfo = ItemDetailUtils.convertToRecord(itemDetailList);

        if (paymentRecord.amount() < orderInfo.totalPrice()) {
            throw new OrderPaymentAmountException("You are underpaying for the order. Please pay " + orderInfo.totalPrice() + " for Order Id: " + paymentRecord.orderId());

        }
        if (paymentRecord.amount() > orderInfo.totalPrice()) {
            throw new OrderPaymentAmountException("You are overpaying for the order. Please pay " + orderInfo.totalPrice() + " for Order Id: " + paymentRecord.orderId());

        }
    }
    // endregion


}
