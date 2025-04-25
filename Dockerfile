FROM eclipse-temurin:17-jdk-alpine
WORKDIR $APP_HOME
EXPOSE 8082
ENV APP_HOME /usr/src/app
COPY target/*.jar $APP_HOME/app.jar
CMD ["java", "-jar", "app.jar"]
