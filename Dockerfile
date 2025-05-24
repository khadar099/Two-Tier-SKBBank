FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8082

ENV APP_HOME /usr/src/app
WORKDIR $APP_HOME

# Copy the specific JAR file and rename it to app.jar for convenience
COPY target/bankapp-0.0.1-SNAPSHOT.jar $APP_HOME/app.jar

# Run the jar with absolute path
CMD ["java", "-jar", "/usr/src/app/app.jar"]
