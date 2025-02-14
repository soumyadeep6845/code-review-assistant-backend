FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/code-review-service-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "./personal/build/libs/personal-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080