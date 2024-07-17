package com.snacks.ordercommands.api.rest;

import com.snacks.ordercommands.api.model.ItemStatusUpdateRecord;
import com.snacks.ordercommands.api.model.OrderInfoRecord;
import com.snacks.ordercommands.api.model.PaymentRecord;
import com.snacks.ordercommands.api.model.ShippingLocationRecord;
import com.snacks.ordercommands.domain.service.ItemDetailService;
import com.snacks.ordercommands.domain.service.PaymentService;
import com.snacks.ordercommands.domain.service.ShippingLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderCommandController {
    private static final Logger log = LoggerFactory.getLogger(OrderCommandController.class);

    private ItemDetailService itemDetailService;
    private ShippingLocationService shippingLocationService;
    private PaymentService paymentService;

    @Autowired
    public OrderCommandController(ItemDetailService itemDetailService, ShippingLocationService shippingLocationService, PaymentService paymentService) {
        this.itemDetailService = itemDetailService;
        this.shippingLocationService = shippingLocationService;
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/api/item-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderInfoRecord postItemDetails(@RequestBody OrderInfoRecord itemDetails) {
        log.info("Entering OrderCommandController.postItemDetails() with itemDetails :: {}", itemDetails);

        OrderInfoRecord savedOrder = itemDetailService.saveNewOrder(itemDetails);

        log.info("Leaving OrderCommandController.postItemDetails()");
        return savedOrder;
    }

    @PutMapping(value = "/api/item-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemStatusUpdateRecord putAllItemStatusUpdate(@RequestBody ItemStatusUpdateRecord itemStatusUpdateRecord) {
        log.info("Entering OrderCommandController.putAllItemStatusUpdate() with itemUpdate :: {}", itemStatusUpdateRecord);

        itemDetailService.updateAllItemsWithStatus(itemStatusUpdateRecord);

        log.info("Leaving OrderCommandController.putAllItemStatusUpdate()");
        return itemStatusUpdateRecord;
    }

    @PostMapping(value = "/api/shipping-location", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShippingLocationRecord postShippingLocation(@RequestBody ShippingLocationRecord shippingLocation) {
        log.info("Entering OrderCommandController.postShippingLocation() with shippingLocatio :: {}", shippingLocation);

        ShippingLocationRecord savedShippingDetails = shippingLocationService.save(shippingLocation);

        log.info("Leaving OrderCommandController.postShippingLocation()");
        return savedShippingDetails;
    }

    @PostMapping(value = "/api/payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentRecord postPayment(@RequestBody PaymentRecord payment) {
        log.info("Entering OrderCommandController.postPayment() with payment :: {}", payment);

        PaymentRecord savedPayment = paymentService.save(payment);

        log.info("Leaving OrderCommandController.postPayment()");
        return savedPayment;
    }
}
