FROM gradle:8.5-jdk21 AS gradle-builder

# Copy source and build files
COPY src /app/src
COPY build.gradle* settings.gradle* /app/
COPY gradlew* /app/
COPY gradle /app/gradle
WORKDIR /app

# Ensure proper permissions and run build
RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon build -x test

FROM openjdk:21

COPY --from=gradle-builder /app/build/libs/*.jar /app-artifact/nextfin.jar
WORKDIR /app-artifact
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "nextfin.jar"]