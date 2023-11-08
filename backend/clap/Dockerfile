# Use an official OpenJDK runtime as a parent image
FROM gradle:8.2.0-jdk17-alpine AS buildimage

WORKDIR /app

COPY gradlew /app/
COPY gradlew.bat /app/
COPY gradle /app/gradle
COPY build.gradle settings.gradle log4j2.xml /app/
COPY src /app/src
COPY proto proto

RUN apk add gcompat
RUN gradle build

FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY --from=buildimage /app /app


EXPOSE 8000
CMD ["java", "-jar", "build/libs/clap.jar"]
