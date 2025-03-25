FROM openjdk:17-jdk-slim
WORKDIR /app
COPY personal/build/libs/personal-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Ensure the database is ready before starting the app
CMD ["sh", "-c", "sleep 10 && java -jar app.jar"]