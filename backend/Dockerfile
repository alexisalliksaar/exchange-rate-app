FROM gradle:8.5-jdk21 AS build
WORKDIR /exrate-back
COPY . .
RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /exrate-back
COPY --from=build /exrate-back/build/libs/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]
