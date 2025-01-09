# Use OpenJDK 17 image as the base for building
FROM openjdk:17-jdk-slim AS build

# Install Maven in the build stage
RUN apt-get update && apt-get install -y maven

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and Maven wrapper files first to leverage Docker cache
COPY pom.xml ./
COPY .mvn .mvn
COPY .mvnw ./

# Ensure that the mvnw file is executable
RUN chmod +x ./mvnw

# Download the Maven dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application
RUN ./mvnw clean install -DskipTests

# Use OpenJDK 17 image for the runtime
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/chat.application-0.0.1-SNAPSHOT.jar /app/chat.application.jar

# Expose the port that Spring Boot is running on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "/app/chat.application.jar"]
