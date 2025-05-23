-- -----------
--Configure Kafka Connectors for the Snack Order Processor
-- -----------
CREATE CATALOG snack_orders_kafka_catalog WITH ('type'='generic_in_memory');
USE CATALOG snack_orders_kafka_catalog;

-- Item Details
CREATE TABLE item_details (
    payload ROW<
        item_id STRING,
        order_id STRING,
        item_name STRING,
        item_status INT,
        price DECIMAL(10,2),
        currency INT,
        quantity INT
    >
) WITH (
    'connector' = 'kafka',
    'topic' = 'order-command-server.order-command-db.item_detail',
    'properties.bootstrap.servers' = 'broker:29092',
    'properties.group.id' = 'flink-snack-order-processor',
    'properties.allow.auto.create.topics' = 'true',
    'scan.startup.mode' = 'earliest-offset', -- Change this as needed

    -- since debezium and we unwrap full state from CDC record
    -- using 'debezium-json' as the format to interpret Debezium JSON messages
    -- is not necessary
    'value.format' = 'json'
);

-- Payment
CREATE TABLE payment (
    payload ROW<
        order_id STRING,
        amount DECIMAL(10,2),
        currency INT,
        payment_type STRING,
        credit_card_type STRING,
        credit_card_number STRING
    >
) WITH (
    'connector' = 'kafka',
    'topic' = 'order-command-server.order-command-db.payment',
    'properties.bootstrap.servers' = 'broker:29092',
    'properties.group.id' = 'flink-snack-order-processor',
    'properties.allow.auto.create.topics' = 'true',
    'scan.startup.mode' = 'earliest-offset', -- Change this as needed

    -- since debezium and we unwrap full state from CDC record
    -- using 'debezium-json' as the format to interpret Debezium JSON messages
    -- is not necessary
    'value.format' = 'json'
);

-- Shipping Location
CREATE TABLE shipping_location (
    payload ROW<
        order_id STRING,
        customer_address STRING,
        customer_name STRING,

        zip_code STRING,
        latitude DOUBLE,
        longitude DOUBLE
    >
) WITH (
    'connector' = 'kafka',
    'topic' = 'order-command-server.order-command-db.shipping_location',
    'properties.bootstrap.servers' = 'broker:29092',
    'properties.group.id' = 'flink-snack-order-processor',
    'properties.allow.auto.create.topics' = 'true',
    'scan.startup.mode' = 'earliest-offset', -- Change this as needed

    -- since debezium and we unwrap full state from CDC record
    -- using 'debezium-json' as the format to interpret Debezium JSON messages
    -- is not necessary
    'value.format' = 'json'
);

-- Customer Order Aggregate
CREATE TABLE customer_order_aggregate (
    orderId STRING PRIMARY KEY NOT ENFORCED, -- upsert-kafka requires a primary key
    orderStatus STRING,
    modifiedDate STRING,
    items ARRAY<
        ROW<
            itemId STRING,
            itemName STRING,
            itemStatus STRING,
            price ROW<
                price DECIMAL(10,2),
                currency STRING
            >,
            quantity INT
        >
    >,
    shippingLocation ROW<
        customerName STRING,
        customerAddress STRING,
        zipCode STRING,
        geoLocation ROW<
            latitude DOUBLE,
            longitude DOUBLE
        >
    >,
    payment ROW<
        paymentType STRING,
        creditCardType STRING,
        creditCardNumber STRING,
        amount ROW<
            amount DECIMAL(10,2),
            currency STRING
        >
    >
) WITH (
    'connector' = 'upsert-kafka',
    'topic' = 'customer-order-aggregate',
    'properties.bootstrap.servers' = 'broker:29092',
    'properties.group.id' = 'flink-snack-order-processor',
    'properties.allow.auto.create.topics' = 'true',
    'key.format' = 'raw',
    'value.format' = 'json'
);

-- -----------
-- Configure MySQL Connectors for the Snack Order Commands
-- -----------
CREATE CATALOG snack_commands_mysql_catalog WITH (
  'type' = 'jdbc',
  'default-database' = 'order-command-db',
  'username' = 'order-command-user',
  'password' = 'password',
  'base-url' = 'jdbc:mysql://mysql_db_server:3306'
);

-- -----------
-- MongoDB Connector for Snack Customer Orders
-- -----------
CREATE CATALOG snack_orders_mongo_catalog WITH ('type'='generic_in_memory');
USE CATALOG snack_orders_mongo_catalog;
CREATE TABLE customerOrder (
    _id STRING,
    customerOrder STRING
) WITH (
    'connector' = 'mongodb',
    'uri' = 'mongodb://mongo-user:password@mongodb_server:27017',
    'database' = 'customer_order_db',
    'collection' = 'customerOrder'
);

-- Switch back to preferred catalog
USE CATALOG snack_orders_kafka_catalog;

