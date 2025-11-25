FROM eclipse-temurin:17-jdk

# Workdir
WORKDIR /app

# Copy Maven wrapper + config
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Use host .m2 to cache dependencies
VOLUME ["/root/.m2"]

# Download dependencies ONLY once
RUN ./mvnw dependency:go-offline -Pdocker

# Copy source (watched by docker-compose)
COPY src ./src

# Expose internal port
EXPOSE 8080

# Dev mode with live reload
ENTRYPOINT ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]
