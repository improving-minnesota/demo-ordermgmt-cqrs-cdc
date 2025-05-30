<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=5 orderedList=false} -->

<!-- code_chunk_output -->

- [Playground - DDD Aggregates w/ CDC and CQRS](#playground---ddd-aggregates-w-cdc-and-cqrs)
  - [Elevator Pitch](#elevator-pitch)
  - [What is in It?](#what-is-in-it)
  - [References](#references)
  - [The Story](#the-story)
    - [Problem](#problem)
    - [Domain Storytelling](#domain-storytelling)
    - [Event Blueprinting](#event-blueprinting)
  - [Solution](#solution)
    - [DDD Context Map](#ddd-context-map)
    - [System Design: Righting Software - "The Method"](#system-design-righting-software---the-method)
    - [C4 Modeling - Context View](#c4-modeling---context-view)
    - [C4 Modeling - Container View](#c4-modeling---container-view)
    - [Implementation: Kafka Streams Topology](#implementation-kafka-streams-topology)
    - [Implementation: Flink SQL Job Overview](#implementation-flink-sql-job-overview)
  - [Playground Environment](#playground-environment)
    - [Kafka-backed Playground](#kafka-backed-playground)
    - [Redpanda-backed Playground](#redpanda-backed-playground)
    - [Warpstream-backed Playground](#warpstream-backed-playground)
    - [Spinning up Playground "Platform"](#spinning-up-playground-platform)
    - [Spinning up Playground "Apps"](#spinning-up-playground-apps)
    - [Stop and Clean the Playground](#stop-and-clean-the-playground)
  - [DEMO](#demo)
    - [PRE: Start Platform and Apps](#pre-start-platform-and-apps)
    - [POST: Stop Platform and Apps](#post-stop-platform-and-apps)
    - [DEMO: API-Driven Demo (POSTMAN)](#demo-api-driven-demo-postman)
    - [DEMO: Snickers Promotion App](#demo-snickers-promotion-app)
    - [Tools During Demo](#tools-during-demo)
  - [From Playground to Arena (i.e. The Cloud)](#from-playground-to-arena-ie-the-cloud)
    - [Deploy on an AWS EC2 Instance](#deploy-on-an-aws-ec2-instance)
      - [Single EC2 Instance Specs](#single-ec2-instance-specs)
      - [Sandbox Setup Steps](#sandbox-setup-steps)
      - [Sandbox Platform & Apps](#sandbox-platform--apps)
      - [Sandbox: Start Instance after Stopping](#sandbox-start-instance-after-stopping)
      - [Monitor Orders w/ FlinkSQL Client](#monitor-orders-w-flinksql-client)
  - [Development Notes (Step-by-Step)](#development-notes-step-by-step)
    - [Build: snack-order-commands (Write)](#build-snack-order-commands-write)
      - [Step 1: Create a Spring Boot project with Gradle, Web, JPA, MySQL](#step-1-create-a-spring-boot-project-with-gradle-web-jpa-mysql)
      - [Step 2: Create the Skeleton OrderController (REST endpoints)](#step-2-create-the-skeleton-ordercontroller-rest-endpoints)
      - [Step 3: Build Components, Tests, Config](#step-3-build-components-tests-config)
    - [Build: snack-order-processor (DDD Aggregate)](#build-snack-order-processor-ddd-aggregate)
      - [Step 1: Create the Spring Boot Base Project](#step-1-create-the-spring-boot-base-project)
      - [Step 2: Create Skeleton Model](#step-2-create-skeleton-model)
      - [Step 3: Configure Kafka and Streams](#step-3-configure-kafka-and-streams)
      - [Step 4: Build Out Components, Tests](#step-4-build-out-components-tests)
      - [Step 5: Setup Kafka Streams Topology Visualization](#step-5-setup-kafka-streams-topology-visualization)
    - [Build: snack-customer-orders (Read)](#build-snack-customer-orders-read)
      - [Step 1: Create a Spring Boot project with Gradle, Web, MongoDB](#step-1-create-a-spring-boot-project-with-gradle-web-mongodb)
      - [Step 2: Create the Skeleton OrderController (REST endpoints)](#step-2-create-the-skeleton-ordercontroller-rest-endpoints-1)
      - [Step 3: Build Components, Tests, Config](#step-3-build-components-tests-config-1)
    - [Setup: Connectors](#setup-connectors)
      - [Step 1: Debezium CDC Connector](#step-1-debezium-cdc-connector)
      - [Step 2: MongoDB Sink Connector](#step-2-mongodb-sink-connector)
  - [Development Troubleshooting Notes](#development-troubleshooting-notes)
    - [UnsatisfiedDependencyException](#unsatisfieddependencyexception)
    - [Dependency error configuring in-memory database for @DataJpaTest](#dependency-error-configuring-in-memory-database-for-datajpatest)
    - [DDL error when running @DataJpaTest](#ddl-error-when-running-datajpatest)
    - [Debezium MySQLConnector Error: Access denied; you need the SUPER, RELOAD, or FLUSH_TABLES privilege for this operation](#debezium-mysqlconnector-error-access-denied-you-need-the-super-reload-or-flush_tables-privilege-for-this-operation)
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
* snack-customer-orders

These services are implemented as Spring-Boot applications in Java.
The `snack-order-commands` exposes API REST endpoints which persist item-details, shipping-info, and payment in their respective tables on MySQL database. 

Debezium tails the MySQL bin logs to capture any events in both these tables and publishes messages to Kafka topics. 

These topics are consumed by `snack-order-processor` which is a Kafka-Streams application that joins data from these topics to create an Order-Aggregate (_CustomerOrder_) 
object which is then published to a `customer-order-aggregate` topic. 

NOTE: The `snack-order-processor` is a Kafka-Streams application. The source code also includes a Flink SQL Job
that can be used as an alternative to Kafka-Streams. The Flink SQL job defined in `snack-order-flinksql` also joins data to create a Customer Order Aggregate
that is inserted into the `customer-order-aggregate` topic.

This `customer-order-aggregate` topic is consumed by MongoDB Sink Connector and the data is persisted in MongoDB which is served by `snack-customer-orders`service.

---

The [Snacks Unlimited "Store" source code](./workspace/snacks-unlimited-order-mgmt) also includes a Next.js frontend application called `snickers-promotion-app`. This is a simple web application that submits order commands for a Snickers. 
It is used for demo purposes.

---

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

### System Design: Righting Software - "The Method" 

![](docs/images/system-design.png)

### C4 Modeling - Context View

![](docs/images/solution-c4-context.png)

### C4 Modeling - Container View

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

### Warpstream-backed Playground

* Get started [Install Warpstream Agent](https://docs.warpstream.com/warpstream/getting-started/install-the-warpstream-agent)

* [Warpstream docker Compose](https://docs.warpstream.com/warpstream/reference/integrations/use-the-agent-in-docker-compose)


### Spinning up Playground "Platform"

The "platform" consists of:

* Confluent Platform (Docker) - Kafka, Kafka Connect
* Apache Flink - SQL Client
* Kafka Connectors for MySQL source and MongoDB sink
* MySQL and "Adminer" (manage MySQL via browser)
* MongoDB and Mongo Express (manage Mongo via browser)

The steps below can be executed from _./workspace_ directory.

```
from <ROOT> directory of this repository
$ cd ./workspace
```

**Start and Stop the Platform**

```
$ ./platform kafka|redpanda|warpstream start
```

```
$ ./platform kafka|redpanda|warpstream stop
```

**Create the MySQL source and MongoDB sink Kafka Connectors**

*NOTE: platform containers must be started and ready.
*NOTE: platform containers must be started and ready.
```
$ ./platform init-connectors
Creating MySQL SourceConnector...
201
Creating MongoDB Sink Connector...
201
```

### Spinning up Playground "Apps"

**Build the Apps**

```
$ ./apps build
$ ./docker-clean
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

### Stop and Clean the Playground
The following commands will stop the docker containers and perform a docker cleanup to remove/prune any dangling docker images, containers, and volumes.

```
$ ./apps stop
$ ./platform kafka|redpanda stop
$ ./docker-clean
```

---

## DEMO

### PRE: Start Platform and Apps

```
-- Demo with Kafka Streams

$ cd .workspace
$ ./platform redpanda|kafka start
$ ./platform init-connectors
Creating MySQL SourceConnector...
201
Creating MongoDB Sink Connector...
201
$ ./apps start --kstreams
```
```
-- Demo with Flink SQL

$ cd .workspace
$ ./platform redpanda|kafka start
$ ./apps start --flinksql
```

### POST: Stop Platform and Apps
```
$ ./apps stop
$ ./platform kafka|redpanda stop
$ ./docker-clean
```

### DEMO: API-Driven Demo (POSTMAN)

See [Snacks Unlimited - Postman Collection](./workspace/postman/UC-Snacks%20Unlimited.postman_collection.json).

--- DEMO START ---

* 1 - Create Kafka Connectors
  * POST Create MySQL CDC Connector
  * POST Create MongoDB Sink Connector

* 2a - Brady's Order
  * POST Snack Items
  * POST Shipping Location
  * POST Payment
* 2b -Oakley's Order
  * POST Snack Items
  * POST Shipping Location
  * POST Payment
* 2c - Rylan's Order
  * POST Snack Items
  * POST Shipping Location
  * POST Payment

* 3 - Fulfill Orders (Sam the Wizard)
  * GET PAID Orders
  * PUT Order Fulfillment

* 4 - Ship Orders (Minions Delivery Team)
  * GET FULFILLED Orders
  * PUT Order Shipment 
  
--- DEMO COMPLETE ---

### DEMO: Snickers Promotion App

* 1- Open Browser to http://{host}:3000
  * Click "WISH I had a Snickers"
![Screen 1](./docs/images/snickers-1.png)
  * Enter your name and Click "HERE I am"
![Screen 1](./docs/images/snickers-2.png)
  * Click "Complete WISH"
![Screen 1](./docs/images/snickers-3.png)
  * Your Snickers Wish is Sent...
![Screen 1](./docs/images/snickers-4.png)

* 2 - Fulfill Orders (Sam the Wizard)
  * GET PAID Orders
  * PUT Order Fulfillment

* 3 - Ship Orders (Minions Delivery Team)
  * GET FULFILLED Orders
  * PUT Order Shipment

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

## From Playground to Arena (i.e. The Cloud)

### Deploy on an AWS EC2 Instance

#### Single EC2 Instance Specs

The EC2 instance needs to have enough CPU, Memory and Storage to run the 12-14 *Snacks Unlimited* containers. 

Based on local *Docker Desktop* stats the following EC2 instance will be a good fit for running the demo containers on AWS. 

```
t3.large
- vCPUs: 2
- Memory: 8GB
- Storage: 30GB
```

#### Sandbox Setup Steps

**Launch EC2 Instance**

```
Go to EC2 Dashboard → Instances → Launch Instance

Name: demo-snacks-unlimited
AMI: Amazon Linux 2 AMI (HVM), SSD Volume Type

Instance type: t3.large
Key pair: Create a New Key pair `demo-snacks-unlimited`

Network settings:
- Default VPN
- Security Group: demo-snacks-unlimited-sg
- Auto Assign Public IP
- Allow SSH (port 22) from your IP
- Allow Next.js App (port 3000) from your IP
- Allow Snacks Unlimited APIs (port 8800-8802) from your IP

Storage: 30 GB

Launch
```

**Connect to EC2**

```
ssh -i /path/to/your-key.pem ec2-user@<public-ip>
```

**Initial Setup Scripts**

```
Install Git and Docker
$ sudo dnf update -y
$ sudo yum update -y
$ sudo yum install git -y

$ sudo dnf install -y docker
$ sudo systemctl enable docker
$ sudo systemctl start docker
$ sudo usermod -aG docker ec2-user

Test Docker
$ docker version
$ docker info

Install Docker Compose
$ mkdir -p ~/.docker/cli-plugins
$ curl -SL https://github.com/docker/compose/releases/download/v2.24.1/docker-compose-linux-x86_64 -o ~/.docker/cli-plugins/docker-compose
$ docker compose version

Install Java
$ sudo dnf install -y java-17-amazon-corretto

$ java --version
OpenJDK Runtime Environment Corretto-17.0.14.7.1 (build 17.0.14+7-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.14.7.1 (build 17.0.14+7-LTS, mixed mode, sharing)

```

**Configure NGINX to Secure Next.js app with HTTPS**

SSH into *demo-snacks-unlimited* EC2 instance.

```
Install nginx
$ sudo yum install nginx -y

Install Certbot (Let's Encrypt SSL tool)
$ sudo yum install certbot python3-certbot-nginx -y
```

**Configure NGINX as reverse proxy. Edit `/etc/nginx/nginx.conf` and add location block:**

```
server {
    listen 80;
    server_name {your-domain.com};

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}

```

**Test**

```
$ sudo nginx -t
$ sudo systemctl enable nginx
$ sudo systemctl start nginx
$ sudo systemctl reload nginx
```

**Get and Install SSL Cert**

This only works IF you register a domain from a registrar such as Route53, Namecheap, GoDaddy, Google Domains.

```
$ sudo certbot --nginx -d your-domain.com -d {your-domain.com}
```

**Clone Snacks Unlimited Demo**

```
$ mkdir ~/demos
$ cd ~/demos
$ git clone https://github.com/improving-minnesota/demo-ordermgmt-cqrs-cdc.git snacks-unlimited-demo
```

#### Sandbox Platform & Apps

```
$ cd ~/demos/snacks-unlimited-demo/workspace
```

**Build App Images**

```
$ ./apps build
$ ./docker-clean
```

**Startup the Platform and Apps**

These are the combinations I found worked on this EC2 instance. Not sure why, but the Flink Job Manager had issues running the Flink SQL job with Redpanda as the broker. NOTE: All combinations worked fine on a Macbook using Docker Desktop. I suspect a network config but I have not looked into it.

```
Combo 1
$ ./platform redpanda start
$ ./apps start --kstreams

Combo 2
$ ./platform kafka start
$ ./apps start --kstreams

Combo 3
$ ./platform kafka start
$ ./apps start --flinksql

Combo 4 (failed)
$ ./platform redpanda start
$ ./apps start --flinksql

MySQL Source Connector and MongoDB Sink Connector
$ ./platform init-connectors

```

#### Sandbox: Start Instance after Stopping


**After Instance is Started**

When stopping the EC2 Instance, on startup the following commands need to run to startup the apps and platforms.  SSH into the instance:

```
$ cd ~/demos/snacks-unlimited-demo/workspace

Clean up to be safe
$ ./platform redpanda|kafka stop
$ ./apps stop
$ ./docker-clean

Start the Platform and Apps 
(See startup combinations in previous section)
```

#### Monitor Orders w/ FlinkSQL Client

```
$ ./apps cli flinksql

// ALL customer order aggregates
Flink SQL> select orderId, orderStatus, shippingLocation.customerName from customer_order_aggregate;

// PAID customer order aggregates
Flink SQL> select orderId, orderStatus, shippingLocation.customerName from customer_order_aggregate where orderStatus = 'PAID';
```

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


