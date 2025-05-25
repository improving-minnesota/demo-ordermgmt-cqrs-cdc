## FAQs

🧱 Domain-Driven Design & Modeling

**1. How did you handle boundaries between aggregates?**
We used DDD context maps to define clear bounded contexts. Aggregates were designed to avoid cross-context writes. Integration was event-based using Kafka to preserve autonomy.

**2. What were the challenges of using DDD with asynchronous systems like Kafka?**
Main challenges were ensuring consistency and ordering. We embraced eventual consistency and relied on idempotent processing, deduplication logic, and schema contracts via Protobuf/Avro.

**3. How deep did you go with tactical DDD patterns?**
We leveraged a domain-model implementation using key DDD building blocks:
* Aggregates, Entities and Value Objects
* Services and Factories
* Repositories
We selectively used Factories for aggregate creation, Repositories to abstract persistence, and Domain Services for business logic. Not all patterns were necessary due to microservice isolation.

📤 CQRS & CDC Patterns

**4. Why CQRS + CDC over CRUD?**
CQRS + CDC provided separation of concerns and enabled scaling write-heavy operations independently. CDC let us derive read models without impacting write-side performance.

**5. How do you prevent CDC bottlenecks or data integrity issues?**
We used Debezium with Kafka Connect, ensuring schema evolution support via a schema registry. Monitoring and alerting were essential for catching lag or transformation issues.

**6. How is eventual consistency and failure handled?**
We implemented compensation logic in consumers. Kafka’s at-least-once delivery ensures no events are missed, and retries handle transient failures.

☁️ Architecture & Infrastructure

**7. Why use both MySQL and MongoDB?**
MySQL was used for transactional consistency on the command side. MongoDB supported flexible, denormalized read models optimized for API consumption.

**8. Kafka Streams vs. FlinkSQL – how do they fit?**
Both process event streams. Kafka Streams offers low-latency, embedded stream processing. FlinkSQL is better for complex transformations and SQL-based ETL in real time.

**9a. What is the difference between Kafka and RedPanda in two sentences?**
Kafka and Redpanda are both event streaming platforms, but Redpanda is a Kafka API-compatible alternative written in C++ with no Java dependency, offering lower latency and better performance on modern hardware. Unlike Kafka, Redpanda claims to have a simpler architecture and is optimized for single-binary deployment and ease of use (SMALLER FOOTPRINT). 

**9b. What is the difference between Kafka and Warpstream in two sentences?**
Kafka is a distributed event streaming platform that stores and processes high-throughput real-time data streams using a self-managed or cloud-hosted cluster. WarpStream is a Kafka-compatible streaming platform that decouples storage and compute, using object storage like S3 to reduce infrastructure cost and simplify scaling.


**10. How is observability achieved?**
We can use OpenTelemetry with centralized logging (Elastic/Datadog), distributed tracing, and Kafka metrics for performance and failure analysis.

🛠️ Implementation & Dev Practices

**11. How do you manage evolving CDC schemas?**
Via versioned topics and schema registry. Backward compatibility is enforced during CI using compatibility checks.

**12. What is Spring Modulith's role?**
It enforces modular boundaries and allows testing module isolation at the package level, helping to avoid unintended dependencies.

**13. How is testing handled?**

Unit tests for domain logic

Integration tests for aggregates and services

Contract tests for Kafka messages using Testcontainers

🧪 Demo & Playground

**14. Can the playground run locally?**
Yes. It is Dockerized and documented for local execution. It can also be deployed to cloud environments for scalability testing.

**15. When is this architecture not a good fit?**
Not ideal for low-latency transactional systems or small apps with simple CRUD needs. Overhead of CQRS/CDC may not justify complexity unless scale or segregation is required.

**[Check it out on GitHub](https://github.com/improving-minnesota/demo-ordermgmt-cqrs-cdc)**

