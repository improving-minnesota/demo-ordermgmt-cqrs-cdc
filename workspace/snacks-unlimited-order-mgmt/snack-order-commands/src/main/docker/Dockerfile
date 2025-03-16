FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="Torey Lomenda"

ARG JAR_FILE=build/libs/snack-order-commands-*-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

# Want the ability to connect to container
RUN apk update && apk add bash

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app.jar"]