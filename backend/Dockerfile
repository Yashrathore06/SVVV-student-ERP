# Step 1: JDK 17 base image
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Step 2: Copy maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Step 3: Package Spring Boot application
RUN ./mvnw clean package -DskipTests

# Step 4: Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]