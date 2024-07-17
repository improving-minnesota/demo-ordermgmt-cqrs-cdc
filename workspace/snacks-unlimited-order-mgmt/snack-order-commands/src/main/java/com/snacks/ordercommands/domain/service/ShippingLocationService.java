package com.snacks.ordercommands.domain.service;

import com.snacks.ordercommands.api.model.ShippingLocationRecord;
import com.snacks.ordercommands.domain.model.entity.ShippingLocation;
import com.snacks.ordercommands.domain.repository.ItemDetailRepository;
import com.snacks.ordercommands.domain.repository.ShippingLocationRepository;
import com.snacks.ordercommands.error.OrderDoesNotExistException;
import com.snacks.ordercommands.utils.ShippingLocationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class ShippingLocationService {

    private static final Logger log = LoggerFactory.getLogger(ShippingLocationService.class);

    ItemDetailRepository itemDetailsRepository;
    ShippingLocationRepository shippingLocationRepository;

    @Autowired
    public ShippingLocationService(ItemDetailRepository itemDetailsRepository, ShippingLocationRepository shippingLocationRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
        this.shippingLocationRepository = shippingLocationRepository;
    }

    public ShippingLocationRecord save(ShippingLocationRecord shippingLocationRecord) {
        log.info("Entering ShippingLocationService.save() with shippingLocationRecord :: {}", shippingLocationRecord);

        validate(shippingLocationRecord);

        ShippingLocation savedShippingLocation = shippingLocationRepository.save(ShippingLocationUtils.convertToEntity(shippingLocationRecord));

        log.info("Leaving ShippingLocationService.save() with savedShippingDetails :: {}", savedShippingLocation);
        return ShippingLocationUtils.convertToRecord(savedShippingLocation);
    }

    // region Private Methods
    private void validate(ShippingLocationRecord shippingLocationRecord) {
        // Needs to be a valid order before setting Shipping Address
        if (ObjectUtils.isEmpty(shippingLocationRecord.orderId()) || itemDetailsRepository.findByOrderId(shippingLocationRecord.orderId()).isEmpty()) {
            throw new OrderDoesNotExistException("Need a valid Order to set shipping address. No Order found for Order Id: " + shippingLocationRecord.orderId());
        }
    }
    // endregion


}
