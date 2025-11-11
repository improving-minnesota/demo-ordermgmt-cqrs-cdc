FROM amazoncorretto:21-alpine
LABEL maintainer="Torey Lomenda"

ARG JAR_FILE=build/libs/snack-order-commands-*-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app.jar"]