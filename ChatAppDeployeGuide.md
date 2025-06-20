# Spring Boot Docker Deployment Guide

## Dockerfile Explanation

### Multi-Stage Docker Build for Spring Boot Application

```dockerfile
# Build Stage
FROM openjdk:17-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory inside container
WORKDIR /app

# Copy project files
COPY pom.xml ./
COPY src ./src

# Run Maven build command
RUN mvn clean install -DskipTests

# Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/chat.application-0.0.1-SNAPSHOT.jar /app/chat.application.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/chat.application.jar"]
```

## Build Stage Breakdown

### Base Image
- `openjdk:17-jdk-slim`: Lightweight Java 17 development image
- Minimal image size
- Contains essential Java development tools

### Maven Installation
- Updates package lists
- Installs Maven for dependency management and build process

### Project Setup
- `WORKDIR /app`: Sets working directory
- `COPY pom.xml ./`: Copies Maven project configuration
- `COPY src ./src`: Copies source code

### Build Process
- `mvn clean install -DskipTests`:
    - Cleans previous builds
    - Compiles project
    - Packages JAR file
    - Skips test execution for faster builds

## Runtime Stage Breakdown

### Base Image
- Same slim OpenJDK 17 image
- Minimal runtime environment

### JAR Deployment
- `COPY --from=build`: Copies JAR from build stage
- Avoids including build-time dependencies

### Container Configuration
- `EXPOSE 8080`: Indicates application port
- `CMD ["java", "-jar"]`: Runs Spring Boot application

## Best Practices

### Performance Optimization
- Multi-stage build reduces image size
- Uses slim base images
- Skips unnecessary build steps

### Build Considerations
- Adjust JAR path if different
- Add environment-specific configurations
- Consider adding health checks

## Sample .dockerignore
```
target/
.git/
.mvn/
*.md
.gitignore
```

## Build and Run Commands
```bash
# Build Docker image
docker build -t your-app-name .

# Run container
docker run -p 8080:8080 your-app-name
```