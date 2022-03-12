FROM adoptopenjdk/openjdk11
MAINTAINER eduardomc94
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
CMD ["/prog", "first argument"]
ENTRYPOINT ["java", "-jar","/app.jar"]