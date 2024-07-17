package com.snacks.ordercommands.domain.repository;

import com.snacks.ordercommands.domain.model.entity.ItemDetail;
import com.snacks.ordercommands.domain.model.entity.OrderItemId;
import com.snacks.ordercommands.domain.model.value.Currency;
import com.snacks.ordercommands.domain.model.value.ItemStatus;
import com.snacks.ordercommands.domain.model.value.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemDetailRepositoryTest {

    @Autowired
    private ItemDetailRepository itemDetailRepository;

    @Test
    public void testSaveAndRetrieveItemDetails() {
        // Given
        ItemDetail itemDetail = new ItemDetail("1", "1", "Item 1", ItemStatus.REQUESTED,new Price(10.0, Currency.USD), 1);

        // When
        itemDetailRepository.save(itemDetail);
        ItemDetail retrievedItemDetails =
                itemDetailRepository.findById(new OrderItemId(itemDetail.getOrderId(), itemDetail.getItemId())).orElse(null);

        // Then
        assertThat(retrievedItemDetails).isNotNull();
        assertThat(retrievedItemDetails.getOrderId()).isEqualTo(itemDetail.getOrderId());
        assertThat(retrievedItemDetails.getItemId()).isEqualTo(itemDetail.getItemId());
        assertThat(retrievedItemDetails.getItemName()).isEqualTo(itemDetail.getItemName());
        assertThat(retrievedItemDetails.getPrice()).isEqualTo(itemDetail.getPrice());
        assertThat(retrievedItemDetails.getQuantity()).isEqualTo(itemDetail.getQuantity());
    }
}
