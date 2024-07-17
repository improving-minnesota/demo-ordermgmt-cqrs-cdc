package com.snacks.ordercommands.utils;

import com.snacks.ordercommands.api.model.ItemDetailRecord;
import com.snacks.ordercommands.api.model.OrderInfoRecord;
import com.snacks.ordercommands.domain.model.entity.ItemDetail;
import com.snacks.ordercommands.domain.model.value.Currency;
import com.snacks.ordercommands.domain.model.value.ItemStatus;
import com.snacks.ordercommands.domain.model.value.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ItemDetailUtils {
    private static final Logger log = LoggerFactory.getLogger(ItemDetailUtils.class);

    // region converters
    public static List<ItemDetail> convertToEntity(OrderInfoRecord orderInfoRecord) {
        log.info("ItemDetailUtils.convertToEntity()");
        return orderInfoRecord.items().stream()
                .map(orderItem -> new ItemDetail(
                    orderInfoRecord.orderId(),
                    orderItem.itemId(), orderItem.itemName(),
                    orderItem.itemStatus() != null ? ItemStatus.valueOf(orderItem.itemStatus()): ItemStatus.REQUESTED,
                    new Price(orderItem.price(), Currency.USD),
                    orderItem.quantity()))
                .collect(Collectors.toList());
    }

    public static OrderInfoRecord convertToRecord(List<ItemDetail> itemDetailsEntities) {
        log.info("ItemDetailUtils.convertToRecord()");
        List<ItemDetailRecord> orderItems = new ArrayList<>();

        itemDetailsEntities.stream().forEach(
                itemDetail -> orderItems.add(new ItemDetailRecord(
                    itemDetail.getItemId(), itemDetail.getItemName(), itemDetail.getItemStatus().name(),
                    itemDetail.getPrice().price(), itemDetail.getQuantity()))
        );

        return new OrderInfoRecord(itemDetailsEntities.get(0).getOrderId(), orderItems);
    }
    //endregion
}
