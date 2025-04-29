package com.snacks.customerorders.domain.service;

import com.snacks.customerorders.domain.model.CustomerOrder;
import com.snacks.customerorders.domain.model.CustomerOrderDocument;
import com.snacks.customerorders.domain.model.value.OrderStatus;
import com.snacks.customerorders.domain.repository.CustomerOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerOrderService {

    private static final Logger log = LoggerFactory.getLogger(CustomerOrderService.class);

    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    public CustomerOrderService(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    public CustomerOrder findByOrderId(String orderId) {
        log.info("Entering CustomerOrderService.findOrderByOrderId() with orderId :: {}", orderId);
        CustomerOrder customerOrder = null;
        CustomerOrderDocument orderDocument = customerOrderRepository.findById(orderId).orElse(new CustomerOrderDocument());
        if (orderDocument.getCustomerOrder() != null) {
            customerOrder = orderDocument.getCustomerOrder();
            log.info("Order retrieved from db: {}", customerOrder);
        }

        return customerOrder;
    }

    public List<CustomerOrder> findByOrderStatus(OrderStatus orderStatus) {
        log.info("Entering CustomerOrderService.findOrderByOrderStatus() with orderStatus :: {}", orderStatus);
        List<CustomerOrder> customerOrders = new ArrayList<>();

        List<CustomerOrderDocument> foundCustomerOrderDocuments =
                customerOrderRepository.findByCustomerOrderOrderStatusOrderByCustomerOrderModifiedDateAsc(orderStatus.name());

        if (!ObjectUtils.isEmpty(foundCustomerOrderDocuments)) {
            foundCustomerOrderDocuments.forEach(customerOrderDocument -> {
                CustomerOrder customerOrder = customerOrderDocument.getCustomerOrder();
                customerOrders.add(customerOrder);
            });
        }

        return customerOrders;
    }

    public List<CustomerOrder> findTop20SnickersPromotions() {
        log.info("Entering CustomerOrderService.findTop20SnickersPromotions()");
        List<CustomerOrder> customerOrders = new ArrayList<>();

        List<CustomerOrderDocument> foundCustomerOrderDocuments = customerOrderRepository.
                findTop20ByCustomerOrderItemsItemNameContainingAndCustomerOrderPaymentCreditCardTypeOrderByCustomerOrderModifiedDateAsc(
                "Snickers",
        "Magical Visa");

        if (!ObjectUtils.isEmpty(foundCustomerOrderDocuments)) {
            foundCustomerOrderDocuments.forEach(customerOrderDocument -> {
                CustomerOrder customerOrder = customerOrderDocument.getCustomerOrder();
                customerOrders.add(customerOrder);
            });
        }

        return customerOrders;
    }
}
