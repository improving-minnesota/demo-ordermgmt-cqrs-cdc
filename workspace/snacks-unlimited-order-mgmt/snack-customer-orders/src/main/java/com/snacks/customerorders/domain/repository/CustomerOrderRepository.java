package com.snacks.customerorders.domain.repository;

import com.snacks.customerorders.domain.model.CustomerOrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends MongoRepository<CustomerOrderDocument, String> {

    List<CustomerOrderDocument> findByCustomerOrderOrderStatus(String orderStatus);

    List<CustomerOrderDocument> findTop20ByCustomerOrderItemsItemNameContainingAndCustomerOrderPaymentCreditCardTypeOrderByCustomerOrderModifiedDateAsc(String item, String creditCardType);
}
