# Step 1: Pre-installed Maven + JDK 17 Image for reliable building
FROM maven:3.8.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Step 2: Copy pom.xml and source code
COPY pom.xml .
COPY src src

# Step 3: Package Spring Boot application using pre-installed Maven
RUN mvn clean package -DskipTests

# Step 4: Lightweight Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]