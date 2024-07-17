package com.snacks.ordercommands.domain.service;

import com.snacks.ordercommands.api.model.ItemStatusUpdateRecord;
import com.snacks.ordercommands.api.model.OrderInfoRecord;
import com.snacks.ordercommands.domain.model.entity.ItemDetail;
import com.snacks.ordercommands.domain.model.value.ItemStatus;
import com.snacks.ordercommands.domain.repository.ItemDetailRepository;
import com.snacks.ordercommands.error.OrderAlreadyExistsException;
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

    @Autowired
    public ItemDetailService(ItemDetailRepository itemDetailsRepository) {
        this.itemDetailRepository = itemDetailsRepository;
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
}
