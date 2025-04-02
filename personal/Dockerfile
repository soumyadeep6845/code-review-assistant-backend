FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the Gradle wrapper and build scripts
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle/

# Copy source code and resources
COPY src src/
COPY src/main/resources src/main/resources/

# Build the JAR inside the container
RUN chmod +x gradlew && ./gradlew build -x test

# Copy the generated JAR
COPY build/libs/personal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Ensure the database is ready before starting the app
CMD ["sh", "-c", "sleep 10 && java -jar app.jar"]