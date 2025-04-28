# Performance Test Runs

## API Performance Tests - Postman

### 10 min Ramp Up, 100 Users

```
Run: Paid Orders for Promotion (items, shipping, payment)

Run Details: 
25 users for 2:30
Ramp to 100 for 2:30
100 users for 5 min
```

#### Runs - Apr 23 

```
Environment: AWS EC2 (t2.large)
Broker: Redpanda
Processor: Kafka Streams App

Total Requests: 101,504
Requests/Sec:   167.27
Avg Response:   93 ms
P90:            175 ms
P95:            205 ms
P99:            261 ms
Error Rate:     0%
```

```
Environment: AWS EC2 (t2.large)
Broker: Kafka
Processor: Kafka Streams App

Total Requests: 100,657
Requests/Sec:   165.91
Avg Response:   95 ms
P90:            182 ms
P95:            216 ms
P99:            284 ms
Error Rate:     0%
```

```
Environment: AWS EC2 (t2.large)
Broker: Kafka
Processor: FlinkSQL Job

Total Requests: 100,056
Requests/Sec:   164.79
Avg Response:   88 ms
P90:            164 ms
P95:            194 ms
P99:            248 ms
Error Rate:     0%
```