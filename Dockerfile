# 1️⃣ Build stage
FROM maven:3.8.4-openjdk-17-slim AS maven_build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package -DskipTests

# 2️⃣ Runtime stage
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

# 3️⃣ Copy the built JAR file from the build stage
COPY --from=maven_build /app/target/Olive-0.0.1-SNAPSHOT.jar app.jar

# 4️⃣ Expose the application port
EXPOSE 8080

# 5️⃣ Define the command to run the application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Dmanagement.metrics.system.enabled=false", "-jar", "app.jar"]
