FROM gradle:8.12.1-jdk17-noble

WORKDIR /app

# Copy the Gradle build files
COPY settings.gradle build.gradle ./
COPY gradle gradle/

# Copy source code and resources
COPY src src/

# Build the JAR inside the container
RUN gradle build -x test

# Copy the generated JAR
COPY build/libs/personal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Ensure the database is ready before starting the app
CMD ["sh", "-c", "sleep 10 && java -jar app.jar"]