FROM node:20-alpine AS frontend
WORKDIR /app
COPY p1-notes-app-frontend/package.json p1-notes-app-frontend/package-lock.json ./
RUN npm ci
COPY p1-notes-app-frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /build
COPY p1-notes-app/pom.xml .
RUN mvn dependency:go-offline
COPY p1-notes-app/src ./src
COPY --from=frontend /app/dist ./src/main/resources/static
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /run
COPY --from=backend /build/target/*.jar ./app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/run/app.jar"] 