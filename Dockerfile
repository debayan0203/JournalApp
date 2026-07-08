# Stage 1: Build the Java 17 application using Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first to utilize Docker caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package the jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application in a lightweight JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy only the built jar from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Launch the application
ENTRYPOINT ["java", "-jar", "app.jar"]