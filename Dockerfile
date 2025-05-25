FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8082

ENV APP_HOME /usr/src/app

COPY target/bankapp-0.0.1-SNAPSHOT.jar $APP_HOME/bankapp-0.0.1-SNAPSHOT.jar

WORKDIR $APP_HOME

# Create logs directory with proper permissions
RUN mkdir -p /usr/src/app/logs && chmod 755 /usr/src/app/logs

CMD ["java", "-jar", "bankapp-0.0.1-SNAPSHOT.jar"]
