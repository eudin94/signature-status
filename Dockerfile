FROM adoptopenjdk/openjdk11
MAINTAINER eduardomc94
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY notifications.sh /notifications.sh
RUN chmod +x /notifications.sh
ENTRYPOINT ["java", "-jar","/app.jar"]