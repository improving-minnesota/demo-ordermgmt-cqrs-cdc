<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=2 orderedList=false} -->

<!-- code_chunk_output -->

- [Playground - DDD Aggregates w/ CDC and CQRS](#playground---ddd-aggregates-w-cdc-and-cqrs)
  - [References](#references)
  - [The Story](#the-story)
  - [Solution](#solution)
  - [Playground Environment](#playground-environment)
  - [DEMO: E2E Script](#demo-e2e-script)
  - [Development Notes (Step-by-Step)](#development-notes-step-by-step)
  - [Development Troubleshooting Notes](#development-troubleshooting-notes)
  - [My Discoveries (What I Learned)](#my-discoveries-what-i-learned)

<!-- /code_chunk_output -->


# Playground - DDD Aggregates w/ CDC and CQRS

## Elevator Pitch

This repository "playground" delivers a solution design with demo-able code that is colored with a fun, fictional story 
providing a "magical journey from context to code using my grandkids love of snacks as inspiration to bring it all together".

Getting acquainted with this material will leave you a certified _Solution Snackitect_ with an understanding
of applying CDC, CQRS, DDD, Kafka Streams, and Flink SQL into an end-to-end solution. 

## What is in It?

This repository is packed with concepts, drawings, slides, and code related to a 
working "playground" building DDD Aggregates via a CDC-CQRS data stream.

The [Presentation Slides](./docs/slides/improvingU-DDD-Aggregates-CQRS-CDC-Kafka.pptx) outline a solution journey
from story (requirements) to solution (architecture & design) to code. The goal of this demo is to 
give _context to code_ and provide a pragmatic approach for designing sofware using
DDD as a key driver.

This demo highlights a CDC-CQRS pipeline between a normalized relational database, MySQL, as the command database and a de-normalized NoSQL database, MongoDB, as the query database resulting in the creation of DDD Aggregates via Debezium & Kafka-Streams.

The [Snacks Unlimited "Store" source code](./workspace/snacks-unlimited-order-mgmt) is centered around three microservices: 
* snack-order-commands
* snack-order-processor
* snack-customer-orders. 

These services are implemented as Spring-Boot applications in Java.
The `snack-order-commands` exposes API REST endpoints which persist item-details, shipping-info, and payment in their respective tables on MySQL database. 

Debezium tails the MySQL bin logs to capture any events in both these tables and publishes messages to Kafka topics. 

These topics are consumed by `snack-order-processor` which is a Kafka-Streams application that joins data from these topics to create an Order-Aggregate (_CustomerOrder_) 
object which is then published to a `customer-order-aggregate` topic. 

This topic is consumed by MongoDB Sink Connector and the data is persisted in MongoDB which is served by `snack-customer-orders`service.

## References

* Source Material
  * [Original Tutorial – Building DDD Aggregates via CQRS/CDC](https://debezium.io/blog/2023/02/04/ddd-aggregates-via-cdc-cqrs-pipeline-using-kafka-and-debezium/)
* Telling the Story - Methods
  * [Domain Storytelling](https://domainstorytelling.org/)
  * [Event Modeling - Blueprinting](https://eventmodeling.org/)
  * [Event Storming](https://www.eventstorming.com/)
  * [User Story Mapping](https://jpattonassociates.com/story-mapping/)
  * [Use Case Modeling](https://www.amazon.com/Use-Case-Modeling-Kurt-Bittner/dp/0201709139)
* Solutioning
  * [Learning Domain-Driven Design](https://www.oreilly.com/library/view/learning-domain-driven-design/9781098100124/)
  * [C4 - Modeling (Arch)](https://c4model.com/)
  * [4+1 Architecture View Model](https://en.wikipedia.org/wiki/4%2B1_architectural_view_model)
* Development
  * [Kafka Streams in Action, Second Edition](https://www.manning.com/books/kafka-streams-in-action-second-edition)
  * [Mastering Kafka Streams and ksqlDB](https://www.oreilly.com/library/view/mastering-kafka-streams/9781492062486/)
  * [Spring Modulith](https://spring.io/projects/spring-modulith)


## The Story

Snacks Unlimited Order Management System. 

Below is a break down of a fictitious but relatable problem and the solution journey to building a scalable, flexible system
using CQRS with CDC and Kadka.


### Problem

![](docs/images/story-problem.png)

### Domain Storytelling

![](docs/images/story-telling.png)

### Event Blueprinting

![](docs/images/story-blueprinting.png)

## Solution

### DDD Context Map

![](docs/images/solution-contextmap.png)

### C4 Modeling - Context View

![](docs/images/solution-c4-context.png)

### C4 Modeling - Component View

![](docs/images/solution-c4-component.png)

### Implementation: Kafka Streams Topology

![](docs/images/kafka-streams-topology.png)

### Implementation: Flink SQL Job Overview

![](docs/images/flinksql-job-overview.png)

---

## Playground Environment

### Kafka-backed Playground
![](docs/images/playground-kafka.png)

### Redpanda-backed Playground
![](docs/images/playground-redpanda.png)

### Spinning up Playground "Platform"

The "platform" consists of:

* Confluent Platform (Docker) - Kafka, Kafka Connect
* Apache Flink - SQL Client
* MySQL and "Adminer" (manage MySQL via browser)
* MongoDB and Mongo Express (manage Mongo via browser)

**Start and Stop the Platform**

```
$ ./platform kafka|redpanda start
```

```
$ ./platform kafka|redpanda stop
```

### Spinning up Playground "Apps"

**Build the Apps**

```
$ ./apps build
```

**Start the Apps with Kafka Streams Processing**

This will start the following Spring Boot applications:

* snack-order-commands (Write / MySQL)
* snack-customer-orders (Read / MongoDB)
* snack-order-processor (Anti-corruption / Kafka Streams)

```
$ ./apps start --kstreams
```

**Start the Apps with FlinkSQL Processing**

This will start the following Spring Boot applications:

* snack-order-commands (Write / MySQL)
* snack-customer-orders (Read / MongoDB)
* FlinkSQL: streaming job with INSERT command

```
$ ./apps start --flinksql
```

**Stop the Apps**

```
$ ./apps stop
```

**Run FlinkSQL Command-Line**

```
$ ./apps cli flinksql
```

### Creating the Debezium CDC MySQL Source Connector (order-command-db)

* In Postman run the _POST Create MySQL CDC Connector_ Request

### Creating the MongoDB Sink Connector (order aggregate store)

* In Postman run the _POST Create MongoDB Sink Connector_ Request

---

## DEMO: E2E Script

See [Snacks Unlimited - Postman Collection](./workspace/postman/UC-Snacks%20Unlimited.postman_collection.json).

### Run Demo

```
-- Demo with Kafka Streams

$ cd .workspace
$ ./platform redpanda|kafka start
$ ./apps start --kstreams
```
```
-- Demo with Flink SQL

$ cd .workspace
$ ./platform redpanda|kafka start
$ ./apps start --flinksql
```

--- DEMO START ---

* 1 - Create Kafka Connectors

* 2 - Create Customer Order (Snack Customer)
  * POST Item Details
  * POST Shipping Location
  * POST Payment

* 3 - Fulfill Orders (Sam the Wizard)
  * GET PAID Orders
  * PUT Order Fulfillment

* 4 - Ship Orders (Minions Delivery Team)
  * GET FULFILLED Orders
  * PUT Order Shipment 
  
--- DEMO COMPLETE ---

### Tools During Demo

* Flink SQL Shell
```
-- Open Flink SQL Shell

$ cd .workspace
$ ./apps flinksql-cli

Flink SQL>
```
* [Flink Dashboard](http://localhost:8881/#/overview)

* Kafka CLI
  * List Kafka Topics
  
  ```
  $ kafka-topics \
      --bootstrap-server $BOOTSTRAP_SERVER \
      --command-config $CC_CONFIG \
      --list
  ```
  
* Consume Kafka Topic Records
  
  ```
  $ kafka-console-consumer \
    --topic "$1" \
    --bootstrap-server $BOOTSTRAP_SERVER \
    --property "schema.registry.url=$SCHEMA_REGISTRY_URL" \
    --property "basic.auth.credentials.source=USER_INFO" \
    --property "basic.auth.user.info=$SCHEMA_AUTH" \
    --property "print.key=true" \
    --property "key.separator= ==> " \
    --consumer.config $CC_CONFIG \
    --consumer-property "group.id=$CONSUMER_GRP" \
    --from-beginning -- OPTIONAL
  ```
  
* [MySQL Database](http://localhost:8180/)
* [MongoDB Database](http://localhost:8181/)

---

## Development Notes (Step-by-Step)

### Build: snack-order-commands (Write)

This is a transactional service that triggers the overall CQRS workflow 
by initiating order commands:

* POST order items (api/item)
* POST shipping details (api/shipping)
* POST payment details (api/payment)

#### Step 1: Create a Spring Boot project with Gradle, Web, JPA, MySQL

```
$ spring init -d=web,jpa,mysql --build=gradle --type=gradle-project-kotlin snack-order-commands
```

Create the base packages:
* api.rest
* data
* exception
* service

#### Step 2: Create the Skeleton OrderController (REST endpoints)

* see src/main/java/api/rest/_OrderWriteController_.java

#### Step 3: Build Components, Tests, Config


### Build: snack-order-processor (DDD Aggregate)

This service consumes from the CDC Topics (triggered from order-write-service) and joins them into an Order Aggregrate for reading.

#### Step 1: Create the Spring Boot Base Project

```
$ spring init -d=web --build=gradle --type=gradle-project-kotlin snack-order-processor
```

Add in the Kafka dependencies in build.gradle.kts:

```
implementation("org.springframework.kafka:spring-kafka")
implementation("org.apache.kafka:kafka-streams")

testImplementation("org.springframework.boot:spring-boot-starter-test")
testImplementation("org.springframework.kafka:spring-kafka-test")
```

Create the base packages:
* config (Kafka Configuration)
* model.domain
* model.inbound
* stream (Kafka Stream topology and Serdes configuration)

#### Step 2: Create Skeleton Model

#### Step 3: Configure Kafka and Streams

#### Step 4: Build Out Components, Tests

#### Step 5: Setup Kafka Streams Topology Visualization

* Add the following line to _OrderAggregateStream_:
```
See TopologyController class
```

* (OPTIONAL) Add this to _application.yml_ to enable actuator endpoints.
```
management:
  endpoints:å
    web:
      exposure:
        include: "*"
```

* From Browser: http://locahost:8801/api/topology

* Paste Input into https://zz85.github.io/kafka-streams-viz/

* View Topology

### Build: snack-customer-orders (Read)

This is a query service that reads from a MongoDB datastore and represents the read segregated responsibilities of CQRS.

Query commands:

* GET api/orders/{orderId}

#### Step 1: Create a Spring Boot project with Gradle, Web, MongoDB

```
$ spring init -d=web,data-mongodb --build=gradle --type=gradle-project-kotlin snack-customer-orders
```

Create the base packages:
* api.rest
* api.model
* data.repository
* service

#### Step 2: Create the Skeleton OrderController (REST endpoints)

* see src/main/java/api/rest/_OrderWriteController_.java

#### Step 3: Build Components, Tests, Config

### Setup: Connectors 

#### Step 1: Debezium CDC Connector

With the _Platform_ Running (See Above).

```
POST http://localhost:8083/connectors
--
{
    "name": "order-command-db-connector",
    "config": {
        "connector.class": "io.debezium.connector.mysql.MySqlConnector",
        "tasks.max": "1",
        "topic.prefix": "order-command-server",
        "database.hostname": "mysql_db_server",
        "database.port": "3306",
        "database.user": "order-command-user",
        "database.password": "password",
        "database.server.id": "142401",
        "database.server.name": "order-command-server",
        "database.whitelist": "order-command-db",
        "table.whitelist": "order-command-db.payment, order-command-db.shipping_location,order-command-db.item_detail",
        "schema.history.internal.kafka.bootstrap.servers": "broker:29092",
        "schema.history.internal.kafka.topic": "dbhistory.order-command-db",
        "include.schema.changes": "true",
        "transforms": "unwrap",
        "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState"
    }
}

```

#### Step 2: MongoDB Sink Connector

With the _Platform_ Running (See Above).

```
POST http://localhost:8083/connectors
--

{
    "name": "order-app-mongo-sink-connector",
    "config": {
        "connector.class": "com.mongodb.kafka.connect.MongoSinkConnector",
        "topics": "customer-order-aggregate",
        "connection.uri": "mongodb://mongo-user:password@mongodb_server:27017",
        "key.converter": "org.apache.kafka.connect.storage.StringConverter",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",
        "value.converter.schemas.enable": false,
        "database": "customer_order_db",
        "collection": "customerOrder",
        "document.id.strategy.overwrite.existing": "true",
        "document.id.strategy": "com.mongodb.kafka.connect.sink.processor.id.strategy.ProvidedInKeyStrategy",
        "transforms": "hk,hv",
        "transforms.hk.type": "org.apache.kafka.connect.transforms.HoistField$Key",
        "transforms.hk.field": "_id",
        "transforms.hv.type": "org.apache.kafka.connect.transforms.HoistField$Value",
        "transforms.hv.field": "customerOrder"
    }
}

```

---

## Development Troubleshooting Notes

### UnsatisfiedDependencyException

```
Iorg.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'dataSourceScriptDatabaseInitializer'
```

This is due to the JPA Data Source not being configured correctly. This can be fixed in the following way:

* Add the data source configuration to application.yml.

```
spring:
  datasource:
    url: jdbc:mysql://localhost:13306/order-command-db
    username: order-command-user
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    generate-ddl: true
    show-sql: true

    hibernate:
      ddl-auto: update
      show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect

```

### Dependency error configuring in-memory database for @DataJpaTest

Add this to your build.gradle.kts:

```
testImplementation("com.h2database:h2")
```

### DDL error when running @DataJpaTest

This annotation starts up an in-memory database to run the JPA repository tests.

In application.yml you do not need this:

```
#        dialect: org.hibernate.dialect.MySQLDialect
```

### Debezium MySQLConnector Error: Access denied; you need the SUPER, RELOAD, or FLUSH_TABLES privilege for this operation

Need to grant privileges for the Debezium MySQLConnector

```
 GRANT ALL PRIVILEGES ON *.* TO 'order-command-user';
 ```

 ---

## My Discoveries (What I Learned)

I learned alot so this is not an exhuastive list.

| Item                                                                                                          | Discovery Notes                                                                                                                                                                                                                                                                                                            |
|---------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Using @DataJpaTest for JPA Repository Tests                                                                   | This annotation bootstraps the test with an in-memory H2 SQL database.                                                                                                                                                                                                                                                     |
| Leverage Spring profiles for _./gradlew bootRun_, _./apps start_ (local-docker), _local-embedded_ for testing | See _src/main/resources/application.yml                                                                                                                                                                                                                                                                                    |
| Access privileges for CDC Connector                                                                           | Check out _./docker/mysql/init/init-db.sql_ to grant privileges to the order-command-user.                                                                                                                                                                                                                                 |
| Flink SQL Client                                                                                              | - tables (connectors) are created in memory and are "gone" after the sessions is closed<br />- The Kafka topics the tables connect to must be created before a Flink Job (using INSERT command) will start corectly.                                                                                                       |
| Flink SQL - 1.19.x vs 1.20.x                                                                                  | This repo uses Flink SQL 1.20.1. Here are some key differences from the last stable version:<br />- 1.20.x supports "ARRAY_AGG" key word making grouping a list of objects easy<br />- 1.20.1 JDBC and MongoDB connectors are not yet available. Keeping an eye on this to connect MySQL and MongoDB from Flink SQL client |
| Redpanda vs Kafka (Dockerized)                                                                                | - Redpanda containers start up quicker and offers seamless "kafka protocol" support for the applications and Flink SQL.<br />- It was fun switching between Confluent Kafka and Redpanda as I experimented with the running demo.                                                                                          |


