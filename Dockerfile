# FROM openjdk 11 jre slim buster
FROM openjdk:17-jdk-slim

# Add VOLUME pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]
