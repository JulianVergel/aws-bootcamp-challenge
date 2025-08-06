# --- Etapa 1: Construcción (Build Stage) ---
# Usamos una imagen de Java 17 que ya incluye Gradle para construir nuestro proyecto.
FROM gradle:8.5-jdk17-alpine AS builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de Gradle para aprovechar la caché de capas de Docker
COPY build.gradle settings.gradle ./
COPY gradle/ ./gradle/

# Copiamos el resto del código fuente
COPY src/ ./src/

# Ejecutamos el comando de Gradle para compilar la aplicación y crear el .jar
RUN gradle build --no-daemon


# --- Etapa 2: Ejecución (Runtime Stage) ---
# Empezamos con una imagen base muy ligera que solo contiene lo necesario para ejecutar Java.
FROM eclipse-temurin:17-jdk-alpine

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos SOLAMENTE el archivo .jar compilado desde la etapa de construcción.
COPY --from=builder /app/build/libs/*.jar app.jar

# Le decimos a Podman que el contenedor escuchará en el puerto 8080 en tiempo de ejecución.
EXPOSE 8080

# El comando que se ejecutará cuando el contenedor arranque.
ENTRYPOINT ["java", "-jar", "app.jar"]