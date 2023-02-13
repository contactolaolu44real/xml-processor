FROM openjdk:17-jdk-slim-buster
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/process-xml.jar
ADD ${JAR_FILE} process-xml.jar
ENTRYPOINT ["java","-jar","process-xml.jar"]