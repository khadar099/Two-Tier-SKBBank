FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8082

ENV APP_HOME=/usr/src/app

# Create the application directory
WORKDIR $APP_HOME

# Copy the JAR file into the container
COPY target/bankapp-0.0.1-SNAPSHOT.jar bankapp-0.0.1-SNAPSHOT.jar

# Create /logs directory where JSON log file will be written
RUN mkdir -p /logs && chmod 755 /logs

# Optional: add a placeholder JSON file (useful for debugging volume mounts)
# RUN touch /logs/skbbank.json

# Run the Spring Boot app
CMD ["java", "-jar", "bankapp-0.0.1-SNAPSHOT.jar"]
