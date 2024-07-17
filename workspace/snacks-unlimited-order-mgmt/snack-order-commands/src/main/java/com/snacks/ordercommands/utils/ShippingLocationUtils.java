package com.snacks.ordercommands.utils;

import com.snacks.ordercommands.api.model.ShippingLocationRecord;
import com.snacks.ordercommands.domain.model.value.GeoLocation;
import com.snacks.ordercommands.domain.model.entity.ShippingLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ShippingLocationUtils {
    private static final Logger log = LoggerFactory.getLogger(ShippingLocationUtils.class);

    // region converters
    public static ShippingLocation convertToEntity(ShippingLocationRecord shippingLocationRecord) {
        ShippingLocation shippingLocation = new ShippingLocation();
        shippingLocation.setOrderId(shippingLocationRecord.orderId());
        shippingLocation.setCustomerName(shippingLocationRecord.customerName());
        shippingLocation.setCustomerAddress(shippingLocationRecord.customerAddress());
        shippingLocation.setZipCode(shippingLocationRecord.zipCode());
        shippingLocation.setGeoLocation(new GeoLocation(shippingLocationRecord.latitude(), shippingLocationRecord.longitude()));

        return shippingLocation;
    }

    public static ShippingLocationRecord convertToRecord(ShippingLocation shippingLocation) {
        ShippingLocationRecord shippingDetailsRecord = new ShippingLocationRecord(
            shippingLocation.getOrderId(),
            shippingLocation.getCustomerName(),
            shippingLocation.getCustomerAddress(),
            shippingLocation.getZipCode(),
            shippingLocation.getGeoLocation().latitude(), shippingLocation.getGeoLocation().longitude());
        return shippingDetailsRecord;
    }
    // endregion
}
