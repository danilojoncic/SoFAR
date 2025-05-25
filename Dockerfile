# --- Stage 1: Build with Gradle and JDK 21 ---
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# Copy project files
COPY --chown=gradle:gradle . .

# Build Spring Boot fat jar
RUN gradle bootJar --no-daemon

# --- Stage 2: Run with Java 21 JRE (small image) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy fat jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
