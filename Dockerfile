# ETAPA 1: Construcción (Generar el .jar con Maven)
FROM maven:3.8.6-openjdk-17 AS build
# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app
# Copia el archivo pom.xml para descargar dependencias
COPY pom.xml .
# Descarga las dependencias
RUN mvn dependency:go-offline
# Copia el resto del código
COPY src ./src
# Construye la aplicación (genera el archivo JAR)
RUN mvn package -DskipTests

# ETAPA 2: Ejecución (Usar el JAR generado)
# Usamos una imagen base más ligera (solo Java Runtime)
FROM openjdk:17-jdk-slim
# Expone el puerto por defecto de Spring Boot
EXPOSE 8080
# Copia el JAR generado de la etapa de construcción
COPY --from=build /app/target/*.jar app.jar
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
