FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY backend/pom.xml backend/pom.xml
COPY backend/src backend/src

RUN mvn -f backend/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/backend/target/pharmasearch-backend-1.0.0.jar app.jar

COPY data data

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
