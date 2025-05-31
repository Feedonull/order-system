# Use a lightweight OpenJDK 17 base image (compatible with Spring Boot 3.x)
FROM amazoncorretto:17

# Set the working directory inside the container
WORKDIR /app

# Copy the fat jar built by Maven/Gradle to the container
COPY target/shop-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (change if needed)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
