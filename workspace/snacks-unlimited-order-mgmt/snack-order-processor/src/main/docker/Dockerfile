# amazoncorretto:21 is used to safely run Kafka Streams Java applications
FROM amazoncorretto:21
LABEL maintainer="Torey Lomenda"

ARG JAR_FILE=build/libs/snack-order-processor-*-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app.jar"]