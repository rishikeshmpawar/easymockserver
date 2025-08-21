# Step 1: Build the application with Gradle
FROM --platform=linux/amd64 gradle:8.10-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN ./gradlew clean bootJar --no-daemon

# Step 2: Run the application with a lightweight JDK
FROM --platform=linux/amd64 eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy only the fat jar built by Spring Boot
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]