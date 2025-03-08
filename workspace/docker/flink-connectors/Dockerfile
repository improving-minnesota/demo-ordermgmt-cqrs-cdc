FROM flink:1.20.1-scala_2.12-java17

# External Jars for Flink Connectors, Sinks, etc
RUN wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-kafka/3.4.0-1.20/flink-connector-kafka-3.4.0-1.20.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-avro-confluent-registry/1.20.1/flink-sql-avro-confluent-registry-1.20.1.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/kafka/kafka-clients/3.9.0/kafka-clients-3.9.0.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-json/1.20.1/flink-json-1.20.1.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-csv/1.20.1/flink-csv-1.20.1.jar; \
    wget -P /opt/flink/lib/ https://github.com/knaufk/flink-faker/releases/download/v0.5.3/flink-faker-0.5.3.jar; \
    \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-jdbc/3.2.0-1.19/flink-connector-jdbc-3.2.0-1.19.jar; \
    wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.31/mysql-connector-j-8.0.31.jar; \
    \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-mongodb/1.2.0-1.19/flink-connector-mongodb-1.2.0-1.19.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/mongodb/mongodb-driver-core/5.0.1/mongodb-driver-core-5.0.1.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/mongodb/mongodb-driver-sync/5.0.1/mongodb-driver-sync-5.0.1.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/mongodb/bson/5.0.1/bson-5.0.1.jar;


RUN chown -R flink:flink /opt/flink/lib
