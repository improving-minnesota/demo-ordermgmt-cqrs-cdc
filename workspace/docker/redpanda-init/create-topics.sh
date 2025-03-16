#!/bin/bash
sleep 5 # Wait for Redpanda to be ready

# Create initial topics for Snack Order Processor
rpk topic create order-command-server.order-command-db.item_detail --partitions 1 --replicas 1 --brokers broker:29092
rpk topic create order-command-server.order-command-db.payment --partitions 1 --replicas 1 --brokers broker:29092
rpk topic create order-command-server.order-command-db.shipping_location --partitions 1 --replicas 1 --brokers broker:29092
rpk topic create customer-order-aggregate -c cleanup.policy=compact --partitions 1 --replicas 1 --brokers broker:29092
