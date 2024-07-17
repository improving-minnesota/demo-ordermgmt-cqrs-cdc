package com.snacks.customerorders.api.rest;

import com.snacks.customerorders.domain.model.CustomerOrder;
import com.snacks.customerorders.domain.model.value.OrderStatus;
import com.snacks.customerorders.domain.service.CustomerOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerOrderController {

    private static final Logger log = LoggerFactory.getLogger(CustomerOrderController.class);

    private CustomerOrderService customerOrderService;

    @Autowired
    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @GetMapping("/api/orders/{orderId}")
    public ResponseEntity<CustomerOrder> findByOrderId(@PathVariable("orderId") String orderId) {
        log.info("Entering OrderController.findByOrderId() with orderId :: {}", orderId);

        CustomerOrder customerOrder = customerOrderService.findByOrderId(orderId);
        if (customerOrder != null) {
            return new ResponseEntity<>(customerOrder, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<CustomerOrder>> findByOrderStatus(@RequestParam(name = "status") String orderStatus) {
        log.info("Entering OrderController.findByOrderStatus() with orderStatus :: {}", orderStatus);

        List<CustomerOrder> customerOrders = customerOrderService.findByOrderStatus(OrderStatus.valueOf(orderStatus));
        if (customerOrders != null) {
            return new ResponseEntity<>(customerOrders, HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }
}
