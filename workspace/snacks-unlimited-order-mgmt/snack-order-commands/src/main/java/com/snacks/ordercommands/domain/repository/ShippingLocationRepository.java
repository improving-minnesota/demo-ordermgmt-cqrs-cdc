package com.snacks.ordercommands.domain.repository;

import com.snacks.ordercommands.domain.model.entity.ShippingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingLocationRepository extends JpaRepository<ShippingLocation, String> {
}
