# Stage 1: Build the Application
FROM gradle:8.14-jdk21 AS builder
WORKDIR /app

# Copy only necessary files first (better caching)
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
COPY src src

# Build the JAR file (skip tests to speed up build, since we test in CI)
RUN ./gradlew bootJar -x test

# Stage 2: Run the Application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]