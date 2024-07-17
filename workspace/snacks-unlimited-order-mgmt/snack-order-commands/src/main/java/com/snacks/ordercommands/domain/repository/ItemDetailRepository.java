package com.snacks.ordercommands.domain.repository;

import com.snacks.ordercommands.domain.model.entity.ItemDetail;
import com.snacks.ordercommands.domain.model.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDetailRepository extends JpaRepository<ItemDetail, OrderItemId> {
    List<ItemDetail> findByOrderId(String orderId);
}
