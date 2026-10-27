# Stage 1: Build stage using Maven and Java 17
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy build descriptor and application sources
COPY pom.xml .
COPY src ./src

# Package application with test verification
RUN mvn -B clean package

# Stage 2: Runtime stage using lightweight Java 17 JRE
FROM eclipse-temurin:17-jre
WORKDIR /app

# Create a dedicated non-root user for security
RUN useradd --system --uid 10001 appuser
COPY --from=build /workspace/target/smart-inventory-0.0.1-SNAPSHOT.jar app.jar
USER 10001

EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
