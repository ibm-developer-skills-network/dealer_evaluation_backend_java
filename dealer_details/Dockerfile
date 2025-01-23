# Use the official Maven image to build the application
FROM maven:3.8.1-openjdk-17-slim AS build
 
# Set the working directory in the container
WORKDIR /app
 
# Copy the pom.xml and other necessary files to the container
COPY pom.xml .
 
# Download dependencies (first step in the multi-stage build)
RUN mvn dependency:resolve dependency:resolve-plugins -B
 
# Copy the source code into the container
COPY src ./src
 
# Build the application
RUN mvn clean package -DskipTests
 
# Use a smaller JRE image to run the application
FROM openjdk:17-jdk-slim
 
# Set the working directory for the runtime container
WORKDIR /app
 
# Copy the built jar file from the build container to the runtime container
COPY --from=build /app/target/dealer_details-0.0.1-SNAPSHOT.war /app/dealer_details-0.0.1-SNAPSHOT.war
 
# Expose the application port (8080)
EXPOSE 8080
 
# Command to run the application
CMD ["java", "-jar", "/app/dealer_details-0.0.1-SNAPSHOT.war"]
 
