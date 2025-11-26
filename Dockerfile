# Use Eclipse Temurin's official OpenJDK 17 image.
# This gives us a clean Linux environment with Java 17 preinstalled.
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container to /app
# All future commands (COPY, RUN, ENTRYPOINT…) operate inside this folder.
WORKDIR /app

# Copy Maven wrapper & configuration files FIRST.
# These rarely change, so Docker can cache this layer.
# This is important because cached layers = faster rebuilds.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Declare a volume for /root/.m2 inside the container.
# This allows Maven dependency caching when mounted via docker-compose.
# Without this, Maven would re-download ALL dependencies every rebuild.
VOLUME ["/root/.m2"]

# Pre-download MAVEN DEPENDENCIES into cache (offline mode)
# Because dependency files rarely change, this is cached.
# -Pdocker activates the docker profile so dependencies match docker environment.
RUN ./mvnw dependency:go-offline -Pdocker

# Copy the entire src directory (your application source code)
# This is the part that changes frequently, so it's placed AFTER dependency caching.
# Any changes to `src` will invalidate only this layer, not the entire build.
COPY src ./src

# Expose port 8080 INSIDE the container.
# This is not the host port — docker-compose handles external mapping.
EXPOSE 8080

# Run the application using Maven in development mode.
# spring-boot:run launches the app with live reload when paired with docker-compose watch.
# The "-Dspring-boot.run.profiles=docker" activates the docker profile.
ENTRYPOINT ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]
