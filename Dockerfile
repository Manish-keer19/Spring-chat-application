# Use OpenJDK 17 as the base image for building
FROM openjdk:17-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory inside container
WORKDIR /app

# Copy your project files
COPY pom.xml ./
COPY src ./src

# Run Maven build command
RUN mvn clean install -DskipTests

# Runtime stage - Copy the built JAR file
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/chat.application-0.0.1-SNAPSHOT.jar /app/chat.application.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/chat.application.jar"]
