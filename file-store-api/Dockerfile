FROM openjdk:21-jdk

VOLUME /working-files
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

LABEL authors="matias"

ENTRYPOINT ["java", "-jar","-DAPPLICATION_CUSTOM=docker","-Dspring.profiles.active=docker","/app.jar"]