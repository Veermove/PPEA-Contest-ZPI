# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim-buster

# Set the working directory to /app
WORKDIR /app

# Copy the Gradle wrapper files (gradlew, gradlew.bat, and the gradle wrapper properties)
COPY gradlew /app/
COPY gradlew.bat /app/
COPY gradle /app/gradle

# Copy the Gradle build files (build.gradle and settings.gradle)
COPY build.gradle settings.gradle log4j2.xml /app/

# Copy the entire source code
COPY src /app/src

# Copy proto files
COPY proto proto

# Build the project with Gradle
RUN ./gradlew build

# Expose port 8080 for the Spring Boot application
EXPOSE 8000

# Define the command to run the Spring Boot application
CMD ["java", "-jar", "build/libs/clap.jar"]
