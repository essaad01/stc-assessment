FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/assessment-0.0.1-SNAPSHOT.jar /app/assessment-0.0.1-SNAPSHOT.jar

EXPOSE 8082

CMD ["java", "-jar", "assessment-0.0.1-SNAPSHOT.jar"]
