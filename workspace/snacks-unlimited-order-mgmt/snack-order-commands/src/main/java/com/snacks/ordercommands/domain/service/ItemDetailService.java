package com.snacks.ordercommands.domain.service;

import com.snacks.ordercommands.api.model.ItemStatusUpdateRecord;
import com.snacks.ordercommands.api.model.OrderInfoRecord;
import com.snacks.ordercommands.domain.model.entity.ItemDetail;
import com.snacks.ordercommands.domain.model.entity.Payment;
import com.snacks.ordercommands.domain.model.entity.ShippingLocation;
import com.snacks.ordercommands.domain.model.value.ItemStatus;
import com.snacks.ordercommands.domain.repository.ItemDetailRepository;
import com.snacks.ordercommands.domain.repository.PaymentRepository;
import com.snacks.ordercommands.domain.repository.ShippingLocationRepository;
import com.snacks.ordercommands.error.OrderAlreadyExistsException;
import com.snacks.ordercommands.error.OrderFullfillmentException;
import com.snacks.ordercommands.error.OrderShippingException;
import com.snacks.ordercommands.utils.ItemDetailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ItemDetailService {
    private static final Logger log = LoggerFactory.getLogger(ItemDetailService.class);

    private ItemDetailRepository itemDetailRepository;
    private ShippingLocationRepository shippingLocationRepository;
    private PaymentRepository paymentRepository;

    @Autowired
    public ItemDetailService(ItemDetailRepository itemDetailRepository, ShippingLocationRepository shippingLocationRepository, PaymentRepository paymentRepository) {
        this.itemDetailRepository = itemDetailRepository;
        this.shippingLocationRepository = shippingLocationRepository;
        this.paymentRepository = paymentRepository;
    }

    public OrderInfoRecord saveNewOrder(OrderInfoRecord orderInfoRecord) {
        log.info("Entering ItemDetailService.saveNewOrder() with OrderInfoRecord :: {}", orderInfoRecord);

        OrderInfoRecord itemDetailsRecordToSave = prepareNewOrderForSave(orderInfoRecord);

        List<ItemDetail> itemDetailsEntities = ItemDetailUtils.convertToEntity(itemDetailsRecordToSave);

        List<ItemDetail> savedItemDetails = itemDetailRepository.saveAll(itemDetailsEntities);

        return ItemDetailUtils.convertToRecord(savedItemDetails);

    }

    public ItemStatusUpdateRecord updateAllItemsWithStatus(ItemStatusUpdateRecord itemStatusUpdateRecord) {
        List<ItemDetail> itemDetailList = itemDetailRepository.findByOrderId(itemStatusUpdateRecord.orderId());

        if (!ObjectUtils.isEmpty(itemDetailList)) {
            validateStatusChange(itemStatusUpdateRecord);

            itemDetailList.forEach(itemDetail -> itemDetail.setItemStatus(ItemStatus.valueOf(itemStatusUpdateRecord.itemStatus())));
            itemDetailRepository.saveAll(itemDetailList);
        }

        return itemStatusUpdateRecord;
    }

    // region Private Methods
    private OrderInfoRecord prepareNewOrderForSave(OrderInfoRecord orderInfoRecord) {
        log.info("ItemDetailsService.prepareToSave()");
        if (ObjectUtils.isEmpty(orderInfoRecord.orderId())) {
            return new OrderInfoRecord(UUID.randomUUID().toString(), orderInfoRecord.items());
        }

        if (!itemDetailRepository.findByOrderId(orderInfoRecord.orderId()).isEmpty()) {
            throw new OrderAlreadyExistsException("Order Id already exists in the database. Order Id: " + orderInfoRecord.orderId());
        }

        return orderInfoRecord;
    }
    // endregion

    // region Validation
    private void validateStatusChange(ItemStatusUpdateRecord itemStatusUpdateRecord) {
        Payment payment = paymentRepository.findByOrderId(itemStatusUpdateRecord.orderId());
        ShippingLocation shippingLocation = shippingLocationRepository.findByOrderId(itemStatusUpdateRecord.orderId());

        if (itemStatusUpdateRecord.itemStatus().equals(ItemStatus.FULFILLED.toString())) {
            if (ObjectUtils.isEmpty(payment)) {
                throw new OrderFullfillmentException("Cannot fulfill order until payment has been processed for Order Id: " + itemStatusUpdateRecord.orderId());
            }
        }

        if (itemStatusUpdateRecord.itemStatus().equals(ItemStatus.SHIPPED.toString())) {
            if (ObjectUtils.isEmpty(shippingLocation) || ObjectUtils.isEmpty(payment)) {
                throw new OrderShippingException("Cannot ship order until payment has been processed and shipping location provided for Order Id: " + itemStatusUpdateRecord.orderId());
            }

        }
    }
}
