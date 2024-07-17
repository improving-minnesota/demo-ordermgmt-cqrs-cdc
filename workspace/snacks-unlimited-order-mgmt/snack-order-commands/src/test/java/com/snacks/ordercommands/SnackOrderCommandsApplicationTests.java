package com.snacks.ordercommands;

import com.snacks.ordercommands.api.model.ItemDetailRecord;
import com.snacks.ordercommands.api.model.OrderInfoRecord;
import com.snacks.ordercommands.api.model.PaymentRecord;
import com.snacks.ordercommands.api.model.ShippingLocationRecord;
import com.snacks.ordercommands.api.rest.OrderCommandController;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
@ActiveProfiles("local-embedded")
class SnackOrderCommandsApplicationTests {
	
	private static final Logger log = LoggerFactory.getLogger(SnackOrderCommandsApplicationTests.class);

	@Autowired
	private OrderCommandController orderController;

	@Test
	void contextLoads() {
	}

	@Test
	void testSaveOrder() {
		OrderInfoRecord order = orderController.postItemDetails(testOrder());
		ShippingLocationRecord shipping = orderController.postShippingLocation(testShipping(order.orderId()));
		PaymentRecord payment = orderController.postPayment(testPayment(order.orderId()));

		// static import for assertThat
		assertThat(order.orderId()).isEqualTo(shipping.orderId());

		log.info("Order ID: {}", order.orderId());
	}

	// region Helper Methods
	private OrderInfoRecord testOrder() {
		ItemDetailRecord item = new ItemDetailRecord("1", "Test Item", "REQUESTED", 0.01, 10);

		return new OrderInfoRecord("", Arrays.asList(item));
	}

	private ShippingLocationRecord testShipping(String orderId) {
		return new ShippingLocationRecord(orderId, "Test User", "Test Address", "55044", 0.0, 0.0);
	}

	private PaymentRecord testPayment(String orderId) {
		return new PaymentRecord(orderId, "Credit Card", "Visa", "1111-2222-3333-4444", 0.10);
	}
	// endregion

}
