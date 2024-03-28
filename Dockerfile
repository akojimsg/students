FROM maven:3-eclipse-temurin-21-alpine AS builder
COPY . /app
WORKDIR /app
RUN mvn clean install -DskipTests

FROM openjdk:21-slim

COPY --from=builder /app/target/students-management-app-0.0.1-SNAPSHOT.jar /students-management-app-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/students-management-app-0.0.1-SNAPSHOT.jar"]