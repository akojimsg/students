FROM maven:3-eclipse-temurin-21-alpine AS builder
COPY . /app
WORKDIR /app
RUN mvn clean install -DskipTests

FROM openjdk:21-slim

COPY --from=builder /app/target/students-0.0.1-SNAPSHOT.jar /students-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/students-0.0.1-SNAPSHOT.jar"]