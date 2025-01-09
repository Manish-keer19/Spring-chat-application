# Use an official OpenJDK image as a base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY . .

# Build the application
RUN ./mvnw clean install -DskipTests

# Expose the port that Spring Boot is running on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "target/chat.application-0.0.1-SNAPSHOT.jar"]
