# Use the Maven image with OpenJDK 17 for the build stage
FROM maven:3.8.6-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and Maven wrapper files first to leverage Docker cache
COPY pom.xml ./
COPY .mvn .mvn
COPY .mvnw ./

# Ensure that the mvnw file is executable
RUN chmod +x .mvnw

# Download the Maven dependencies (this will also help avoid re-downloading on every build)
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application
RUN ./mvnw clean install -DskipTests

# Use OpenJDK 17 image for the runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/chat.application-0.0.1-SNAPSHOT.jar /app/chat.application.jar

# Expose the port that Spring Boot is running on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "/app/chat.application.jar"]
