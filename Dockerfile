# Etap 1: Build frontend
FROM node:18 AS frontend-builder

WORKDIR /app/frontend

# Kopiujemy tylko package.json i locka na początek, by użyć cache
COPY frontend/package*.json ./

RUN npm install

# Teraz dopiero cały kod frontendu
COPY frontend/ .

RUN npm run build

# Etap 2: Build backend (Spring Boot)
FROM eclipse-temurin:17-jdk-alpine AS backend-builder

WORKDIR /app

COPY backend ./backend
COPY --from=frontend-builder /app/frontend/build ./backend/src/main/resources/static

WORKDIR /app/backend
RUN chmod +x ./gradlew && ./gradlew build

# Etap 3: Final image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=backend-builder /app/backend/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
