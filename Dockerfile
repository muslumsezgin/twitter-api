FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 9600

ARG JAR_FILE=target/twitter-api-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} twitter-api.jar

ENTRYPOINT ["java","-jar","/twitter-api.jar"]