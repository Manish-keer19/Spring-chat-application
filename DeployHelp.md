# Deploying Spring Boot Application with Docker on Render

## Prerequisites
- Spring Boot application
- Docker
- GitHub repository with your project
- Render account

## Step 1: Dockerize Your Spring Boot Application

### Create Dockerfile
Create a `Dockerfile` in your project root:

```dockerfile
# Use official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy Maven/Gradle build artifact
COPY target/*.jar app.jar

# Expose port your app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

### Create .dockerignore
```
target/
.git/
.gradle/
.idea/
*.md
```

## Step 2: Prepare GitHub Repository
- Ensure Dockerfile is committed
- Push your code to GitHub

## Step 3: Render Deployment Setup

### Docker Deployment Options
1. **Web Service (Recommended)**
    - Supports automatic deploys from GitHub
    - Free tier available

2. **Blueprint Deployment**
    - More complex configuration
    - Advanced networking options

### Render Configuration Steps
1. Log into Render Dashboard
2. Click "New Web Service"
3. Choose "Docker" deployment type
4. Connect GitHub repository
5. Configure deployment settings:
    - **Branch**: `main` or your primary branch
    - **Dockerfile Path**: `./Dockerfile`
    - **Docker Build Context**: `./`

### Environment Variables
- Add database connection strings
- Set Spring profiles
- Configure any external service credentials

## Advanced Configuration

### Multi-stage Docker Build
For optimized builds:

```dockerfile
# Build stage
FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```

## Common Troubleshooting
- Verify Docker build locally first
- Check Render logs for deployment issues
- Ensure correct port configuration
- Validate environment variables

## Performance Tips
- Use slim base images
- Minimize layer count
- Leverage Docker build caching

## Monitoring
- Use Render metrics
- Set up external monitoring services
```

Would you like me to elaborate on any specific section of the deployment guide?