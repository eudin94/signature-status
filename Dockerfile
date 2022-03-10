FROM adoptopenjdk/openjdk11
MAINTAINER eduardomc94
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar","/app.jar"]