FROM openjdk:17-jdk-slim

WORKDIR /app

COPY src /app/src

RUN  mvn package

EXPOSE 8082

CMD ["java", "-jar", "assessment-0.0.1-SNAPSHOT.jar"]
