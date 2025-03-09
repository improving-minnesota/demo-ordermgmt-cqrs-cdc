#!/bin/bash
sleep 5 # Wait for Redpanda to be ready

# Create initial topics for Snack Order Processor
kafka-topics --create --topic order-command-server.order-command-db.item_detail --partitions 1 --replication-factor 1 --if-not-exists --bootstrap-server broker:29092
kafka-topics --create --topic order-command-server.order-command-db.payment --partitions 1 --replication-factor 1 --if-not-exists --bootstrap-server broker:29092
kafka-topics --create --topic order-command-server.order-command-db.shipping_location --partitions 1 --replication-factor 1 --if-not-exists --bootstrap-server broker:29092
kafka-topics --create --topic customer-order-aggregate --partitions 1 --replication-factor 1 --if-not-exists --bootstrap-server broker:29092
