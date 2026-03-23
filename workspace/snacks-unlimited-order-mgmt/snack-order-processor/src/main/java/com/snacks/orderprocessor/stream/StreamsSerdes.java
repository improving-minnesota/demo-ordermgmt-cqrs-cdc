package com.snacks.orderprocessor.stream;

import com.snacks.orderprocessor.model.domain.CustomerOrder;
import com.snacks.orderprocessor.model.inbound.ItemDetailRecord;
import com.snacks.orderprocessor.model.inbound.PaymentRecord;
import com.snacks.orderprocessor.model.inbound.ShippingLocationRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.core.type.TypeReference;

import java.util.ArrayList;

public class StreamsSerdes extends Serdes {

	public static Serde<ItemDetailRecord> ItemDetailRecordSerde() {
		return new ItemDetailRecordSerde();
	}

	public static final class ItemDetailRecordSerde extends WrapperSerde<ItemDetailRecord> {
		public ItemDetailRecordSerde() {
			super(new JacksonJsonSerializer<>(), new JacksonJsonDeserializer<>(ItemDetailRecord.class, false));
		}
	}

	public static Serde<ArrayList<ItemDetailRecord>> ItemDetailRecordArrayListSerde() {
		return new ItemDetailRecordArrayListSerde();
	}

	public static final class ItemDetailRecordArrayListSerde extends WrapperSerde<ArrayList<ItemDetailRecord>> {

		public ItemDetailRecordArrayListSerde() {
			super(new JacksonJsonSerializer<>(), new JacksonJsonDeserializer<>(new TypeReference<ArrayList<ItemDetailRecord>>() {}, false));
		}
	}

	public static Serde<ShippingLocationRecord> ShippingLocationRecordSerde() {
		return new ShippingLocationRecordSerde();
	}

	public static final class ShippingLocationRecordSerde extends WrapperSerde<ShippingLocationRecord> {
		public ShippingLocationRecordSerde() {
			super(new JacksonJsonSerializer<>(), new JacksonJsonDeserializer<>(ShippingLocationRecord.class, false));
		}
	}

	public static Serde<PaymentRecord> PaymentRecordSerde() {
		return new PaymentRecordSerde();
	}

	public static final class PaymentRecordSerde extends WrapperSerde<PaymentRecord> {
		public PaymentRecordSerde() {
			super(new JacksonJsonSerializer<>(), new JacksonJsonDeserializer<>(PaymentRecord.class, false));
		}
	}

	public static Serde<CustomerOrder> CustomerOrderSerde() {
		return new CustomerOrderSerde();
	}

	public static final class CustomerOrderSerde extends WrapperSerde<CustomerOrder> {
		public CustomerOrderSerde() {
			super(new JacksonJsonSerializer<>(), new JacksonJsonDeserializer<>(CustomerOrder.class, false));
		}
	}

}
