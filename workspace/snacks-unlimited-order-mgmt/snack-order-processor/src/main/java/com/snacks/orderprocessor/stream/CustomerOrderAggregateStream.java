package com.snacks.orderprocessor.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snacks.orderprocessor.model.domain.CustomerOrder;
import com.snacks.orderprocessor.model.domain.ItemDetail;
import com.snacks.orderprocessor.model.domain.Payment;
import com.snacks.orderprocessor.model.domain.ShippingLocation;
import com.snacks.orderprocessor.model.domain.value.Amount;
import com.snacks.orderprocessor.model.domain.value.Currency;
import com.snacks.orderprocessor.model.domain.value.GeoLocation;
import com.snacks.orderprocessor.model.domain.value.ItemStatus;
import com.snacks.orderprocessor.model.domain.value.Price;
import com.snacks.orderprocessor.model.inbound.ItemDetailRecord;
import com.snacks.orderprocessor.model.inbound.PaymentRecord;
import com.snacks.orderprocessor.model.inbound.ShippingLocationRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CustomerOrderAggregateStream {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerOrderAggregateStream.class);

    // region SERDE (Serialize/Deserialize properties)
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<ItemDetailRecord> ITEM_RECORD_SERDE = StreamsSerdes.ItemDetailRecordSerde();

    private static final Serde<ArrayList<ItemDetailRecord>> ITEM_RECORD_ARRAYLIST_SERDE = StreamsSerdes.ItemDetailRecordArrayListSerde();
    private static final Serde<ShippingLocationRecord> SHIPPING_LOCATION_RECORD_SERDE = StreamsSerdes.ShippingLocationRecordSerde();
    private static final Serde<PaymentRecord> PAYMENT_RECORD_SERDE = StreamsSerdes.PaymentRecordSerde();
    private static final Serde<CustomerOrder> CUSTOMER_ORDER_AGGREGATE_RECORD_SERDE = StreamsSerdes.CustomerOrderSerde();
    // endregion

    // region Streams State Stores
    private static final String ITEM_RECORD_STATE_STORE = "orderitem-state-store";
    private static final String SHIPPING_LOCATION_RECORD_STATE_STORE = "shipping-location-state-store";
    private static final String PAYMENT_RECORD_STATE_STORE = "payment-state-store";
    // endregion

    // region Object Mappper
    private final ObjectMapper objectMapper = new ObjectMapper();

    // endregion

    // region Topic Properties

    @Value("#{kafkaConfig.itemDetailTopicName()}")
    private String itemDetailsTopicName;

    @Value("#{kafkaConfig.shippingLocationTopicName()}")
    private String shippingLocationTopicName;

    @Value("#{kafkaConfig.paymentTopicName()}")
    private String paymentTopicName;

    @Value("#{kafkaConfig.customerOrderAggregateTopicName()}")
    private String customerOrderAggregateTopicName;

    // endregion

    // region Kafka Streams

    @Autowired
    public void processMessage(StreamsBuilder streamsBuilder) {
        // **** Read Shipping Location ****
        KTable<String, ShippingLocationRecord> shippingLocationKTable = consumeShippingLocationToKTable(streamsBuilder);

        // **** Read Payment ****
        KTable<String, PaymentRecord> paymentKTable = consumePaymentToKTable(streamsBuilder);

        // **** Read Order Item Details ****
        KTable<String, ArrayList<ItemDetailRecord>> orderItemsKTable = consumeItemsToKTable(streamsBuilder);

        // **** Join Order Items and Shipping Location and Payment, Publish Customer Order Aggregate ****
        joinAndPublishCustomerOrder(streamsBuilder, shippingLocationKTable, paymentKTable, orderItemsKTable);
    }

    private KTable<String, ShippingLocationRecord> consumeShippingLocationToKTable(StreamsBuilder streamsBuilder) {
        KStream<String, String> shippingLocationSourceStream = streamsBuilder.stream(
            shippingLocationTopicName, Consumed.with(STRING_SERDE, STRING_SERDE)
        );

        // Convert value to ShippingDetailsRecord
        KStream<String, ShippingLocationRecord> shippingLocationRecordWithOrderIdStream = shippingLocationSourceStream
            .map((orderIdJson, shippingLocationJson) -> new KeyValue<>(parseOrderId(orderIdJson), parseShippingLocation(shippingLocationJson)));

        // Put into a KTable for Joining
        KTable<String, ShippingLocationRecord> shippingDetailsKTable = shippingLocationRecordWithOrderIdStream
            .toTable(Materialized.<String, ShippingLocationRecord, KeyValueStore<Bytes, byte[]>>as(SHIPPING_LOCATION_RECORD_STATE_STORE)
                .withKeySerde(STRING_SERDE)
                .withValueSerde(SHIPPING_LOCATION_RECORD_SERDE));

        // Print KTable
        shippingDetailsKTable.toStream().foreach((key, value) -> log.info("Shipping Location KTable :: Key {} :: Value {}.", key, value));

        return shippingDetailsKTable;
    }

    private KTable<String, PaymentRecord> consumePaymentToKTable(StreamsBuilder streamsBuilder) {
        KStream<String, String> paymentSourceStream = streamsBuilder.stream(
                paymentTopicName, Consumed.with(STRING_SERDE, STRING_SERDE)
        );

        // Convert value to ShippingDetailsRecord
        KStream<String, PaymentRecord> paymentRecordWithOrderIdStream = paymentSourceStream
                .map((orderIdJson, paymentJson) -> new KeyValue<>(parseOrderId(orderIdJson), parsePayment(paymentJson)));

        // Put into a KTable for Joining
        KTable<String, PaymentRecord> paymentKTable = paymentRecordWithOrderIdStream
                .toTable(Materialized.<String, PaymentRecord, KeyValueStore<Bytes, byte[]>>as(PAYMENT_RECORD_STATE_STORE)
                        .withKeySerde(STRING_SERDE)
                        .withValueSerde(PAYMENT_RECORD_SERDE));

        // Print KTable
        paymentKTable.toStream().foreach((key, value) -> log.info("Payment KTable :: Key {} :: Value {}.", key, value));

        return paymentKTable;
    }

    private KTable<String, ArrayList<ItemDetailRecord>> consumeItemsToKTable(StreamsBuilder streamsBuilder) {
        KStream<String, String> itemDetailSourceStream = streamsBuilder.stream(
            itemDetailsTopicName, Consumed.with(STRING_SERDE, STRING_SERDE)
        );

        KStream<String, ItemDetailRecord> itemDetailWithOrderIdStream = itemDetailSourceStream
            .map((orderIdJson, orderItemJson) -> new KeyValue<>(parseOrderId(orderIdJson), parseOrderItem(orderItemJson)));

        // Group Order Item Records by OrderId
        KGroupedStream<String, ItemDetailRecord> itemGroupedStream = itemDetailWithOrderIdStream
            .groupByKey(Grouped.with(STRING_SERDE, ITEM_RECORD_SERDE));

        // Aggregate all OrderItem Records by OrderId into a List
        KTable<String, ArrayList<ItemDetailRecord>> itemListKTable = itemGroupedStream.aggregate(
            (Initializer<ArrayList<ItemDetailRecord>>) ArrayList::new,
            (orderId, item, itemList) -> addOrderItem(itemList, item),
            Materialized.<String, ArrayList<ItemDetailRecord>, KeyValueStore<Bytes, byte[]>>as(ITEM_RECORD_STATE_STORE)
                .withKeySerde(STRING_SERDE)
                .withValueSerde(ITEM_RECORD_ARRAYLIST_SERDE));

        itemListKTable.toStream().foreach((key, value) -> log.info("Item List KTable :: Key {} :: Value {}.", key, value));

        return itemListKTable;
    }

    private void joinAndPublishCustomerOrder(StreamsBuilder streamsBuilder,
                                             KTable<String, ShippingLocationRecord> shippingLocationKTable,
                                             KTable<String, PaymentRecord> paymentKTable,
                                             KTable<String, ArrayList<ItemDetailRecord>> itemsKTable) {

        // Joining the two tables: shippingLocationKTable and itemsKTable (need items and shipping details)
        // Left Join Customer Order with Items, Shipping, and Payment
        ValueJoiner<ShippingLocationRecord, ArrayList<ItemDetailRecord>, CustomerOrder> shippingAndItemListJoiner =
                (shippingLocationRecord, itemRecordList) -> buildInitialCustomerOrder(shippingLocationRecord, itemRecordList);
        ValueJoiner<CustomerOrder, PaymentRecord, CustomerOrder> customerOrderWithPaymentJoiner =
                (customerOrder, paymentRecord) -> buildCustomerOrderWithPayment(customerOrder, paymentRecord);

        KTable<String, CustomerOrder> customerOrderKTable = shippingLocationKTable
                .join(itemsKTable, shippingAndItemListJoiner)
                .leftJoin(paymentKTable, customerOrderWithPaymentJoiner);


        // Outputting to Kafka Topic
        customerOrderKTable.toStream().to(customerOrderAggregateTopicName, Produced.with(STRING_SERDE, CUSTOMER_ORDER_AGGREGATE_RECORD_SERDE));
    }
    // endregion

    // region Inbound Record Parsing Helpers
    private String parseOrderId(String orderIdJson) {
        String orderId = null;

        try {
            JsonNode keyNode = objectMapper.readTree(orderIdJson);
            JsonNode objectIdNode = keyNode.at("/payload/order_id");

            orderId = objectIdNode.asText();
        } catch (JsonMappingException e) {
            log.error("JsonMappingException: ", e);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: ", e);
        }

        return orderId;
    }

    private ItemDetailRecord parseOrderItem(String itemJson) {
        ItemDetailRecord item = null;
        try {
            JsonNode orderItemRoot = objectMapper.readTree(itemJson);

            JsonNode payloadNode = orderItemRoot.get("payload");

            String itemId = payloadNode.get("item_id").asText();
            String itemName = payloadNode.get("item_name").asText();
            String itemStatus = payloadNode.get("item_status").asText();
            Double price = payloadNode.get("price").asDouble(0);
            Integer quantity = payloadNode.get("quantity").asInt(0);

            item = new ItemDetailRecord(itemId, itemName, itemStatus, price, quantity);

            log.info("Item Detail Status :: INT {} :: ENUM {}.", itemStatus, ItemStatus.values()[Integer.parseInt(itemStatus)]);

        } catch (JsonMappingException e) {
            log.error("JsonMappingException: ", e);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: ", e);
        }

        return item;
    }

    private ShippingLocationRecord parseShippingLocation(String shippingLocationJson) {
        ShippingLocationRecord shippingLocationRecord = null;

        try {
            JsonNode shippingLocationRoot = objectMapper.readTree(shippingLocationJson);
            JsonNode payloadNode = shippingLocationRoot.get("payload");
            String orderId = payloadNode.get("order_id").asText();
            String customerAddress = payloadNode.get("customer_address").asText();
            String customerName = payloadNode.get("customer_name").asText();
            String zipCode = payloadNode.get("zip_code").asText();
            Double latitude = payloadNode.get("latitude").asDouble();
            Double longitude = payloadNode.get("longitude").asDouble();

            shippingLocationRecord = new ShippingLocationRecord(orderId, customerName, customerAddress, zipCode, latitude, longitude);

        } catch (JsonMappingException e) {
            log.error("JsonMappingException: ", e);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: ", e);
        }

        return shippingLocationRecord;
    }

    private PaymentRecord parsePayment(String paymentJson) {
        PaymentRecord paymentRecord = null;

        try {
            JsonNode paymentRoot = objectMapper.readTree(paymentJson);
            JsonNode payloadNode = paymentRoot.get("payload");
            String orderId = payloadNode.get("order_id").asText();
            String paymentType = payloadNode.get("payment_type").asText();
            String creditCardType = payloadNode.get("credit_card_type").asText();
            String creditCardNumber = payloadNode.get("credit_card_number").asText();
            Double amount = payloadNode.get("amount").asDouble();

            paymentRecord = new PaymentRecord(orderId, paymentType, creditCardType, creditCardNumber, amount);

        } catch (JsonMappingException e) {
            log.error("JsonMappingException: ", e);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: ", e);
        }

        return paymentRecord;
    }

    private ArrayList<ItemDetailRecord> addOrderItem(ArrayList<ItemDetailRecord> orderItemList, ItemDetailRecord orderItem) {
        // Want to make sure that each itemId is indexed once in the list
        int foundIndex = IntStream.range(0, orderItemList.size())
                .filter(i -> orderItemList.get(i).itemId().equals(orderItem.itemId()))
                .findFirst()
                .orElse(-1);

        if (foundIndex == -1) {
            orderItemList.add(orderItem);
        } else {
            orderItemList.set(foundIndex, orderItem);
        }

        return orderItemList;
    }

    // endregion

    // region Aggregate Builder Methods
    private CustomerOrder buildInitialCustomerOrder(ShippingLocationRecord shippingLocationRecord, ArrayList<ItemDetailRecord> orderItemList) {
       String orderId = shippingLocationRecord.orderId();
        ShippingLocation shippingLocation = new ShippingLocation(
                shippingLocationRecord.customerName(),
                shippingLocationRecord.customerAddress(),
                shippingLocationRecord.zipCode(),
                new GeoLocation(shippingLocationRecord.latitude(), shippingLocationRecord.longitude())
        );

        List<ItemDetail> items = new ArrayList<>();
        for (ItemDetailRecord itemDto : orderItemList) {
            ItemDetail item = new ItemDetail(
                itemDto.itemId(),
                itemDto.itemName(),
                ItemStatus.values()[Integer.parseInt(itemDto.itemStatus())],
                new Price(itemDto.price(), Currency.USD),
                itemDto.quantity()
            );

            items.add(item);
        }

        CustomerOrder order = new CustomerOrder(orderId, shippingLocation, items);
        return order;
    }

    private CustomerOrder buildCustomerOrderWithPayment(CustomerOrder customerOrder, PaymentRecord paymentRecord) {
        if (paymentRecord != null) {
            String orderId = paymentRecord.orderId();
            Payment payment = new Payment(
                    paymentRecord.paymentType(),
                    paymentRecord.creditCardType(),
                    paymentRecord.creditCardNumber(),
                    new Amount(paymentRecord.amount(), Currency.USD)
            );


            CustomerOrder customerOrderWithPayment = new CustomerOrder(orderId, customerOrder.getShippingLocation(), payment, customerOrder.getItems());
            return customerOrderWithPayment;
        }

        return customerOrder;

    }

    // endregion
}
