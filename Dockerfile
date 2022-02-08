FROM openjdk:8

EXPOSE 9025

WORKDIR /

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} translator-gateway.jar

ARG RESOURCES_FOLDER=src/main/resources/*

COPY ${RESOURCES_FOLDER} /resources/

ENTRYPOINT ["java", "-jar", "translator-gateway.jar"]