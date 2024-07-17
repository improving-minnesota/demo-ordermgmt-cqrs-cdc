package com.snacks.orderprocessor.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.streams.input.item-detail.topic-name}")
	private String itemDetailTopicName;

	@Value("${spring.kafka.streams.input.shipping-location.topic-name}")
	private String shippingLocationTopicName;

	@Value("${spring.kafka.streams.input.payment.topic-name}")
	private String paymentTopicName;

	@Value("${spring.kafka.streams.input.customer-order-aggregate.topic-name}")
	private String customerOrderAggregateTopicName;

	@Bean
	public String shippingLocationTopicName() {
		return shippingLocationTopicName;
	}

	@Bean
	public String paymentTopicName() {
		return paymentTopicName;
	}

	@Bean
	public String itemDetailTopicName() {
		return itemDetailTopicName;
	}

	@Bean
	public String customerOrderAggregateTopicName() {
		return customerOrderAggregateTopicName;
	}

	@Bean
	public NewTopic createShippingLocationTopicName() {
		return TopicBuilder.name(shippingLocationTopicName).build();
	}

	@Bean
	public NewTopic createPaymentTopicName() {
		return TopicBuilder.name(paymentTopicName).build();
	}


	@Bean
	public NewTopic createItemDetailTopicName() {
		return TopicBuilder.name(itemDetailTopicName).build();
	}

	@Bean
	public NewTopic createCustomerOrderAggregateTopicName() {
		return TopicBuilder.name(customerOrderAggregateTopicName).build();
	}

}
