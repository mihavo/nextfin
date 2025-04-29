FROM maven:latest as maven-builder

COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package -DskipTests

FROM openjdk:21

COPY --from=maven-builder /app/target/*.jar /app-artifact/nextfin.jar
WORKDIR /app-artifact
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "nextfin.jar"]