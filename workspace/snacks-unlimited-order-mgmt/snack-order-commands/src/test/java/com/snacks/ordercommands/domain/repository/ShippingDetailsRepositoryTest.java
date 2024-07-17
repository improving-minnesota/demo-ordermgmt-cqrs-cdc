package com.snacks.ordercommands.domain.repository;

import com.snacks.ordercommands.domain.model.value.GeoLocation;
import com.snacks.ordercommands.domain.model.entity.ShippingLocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ShippingDetailsRepositoryTest {

    @Autowired
    private ShippingLocationRepository shippingLocationRepository;

    @Test
    public void testSaveAndRetrieveShippingDetails() {
        // Given
        ShippingLocation shippingLocation = new ShippingLocation("123",
                "Torey Lomenda", "19152 Harappa Ave", "55044",
                new GeoLocation(86.0, 77.0));

        // When
        shippingLocationRepository.save(shippingLocation);
        ShippingLocation retrievedShipping = shippingLocationRepository.findById(shippingLocation.getOrderId()).orElse(null);

        // Then
        assertThat(retrievedShipping).isNotNull();
        assertThat(retrievedShipping.getOrderId()).isEqualTo(shippingLocation.getOrderId());
        assertThat(retrievedShipping.getCustomerName()).isEqualTo(shippingLocation.getCustomerName());
        assertThat(retrievedShipping.getCustomerAddress()).isEqualTo(shippingLocation.getCustomerAddress());
        assertThat(retrievedShipping.getZipCode()).isEqualTo(shippingLocation.getZipCode());
        assertThat(retrievedShipping.getGeoLocation()).isEqualTo(shippingLocation.getGeoLocation());
    }


}
