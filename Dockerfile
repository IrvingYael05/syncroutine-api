# Etapa 1: Compilación
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
# Copiamos los archivos necesarios
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
# Le damos permisos de ejecución al wrapper de Maven
RUN chmod +x ./mvnw
# Compilamos el proyecto omitiendo los tests para mayor velocidad
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos solo el archivo .jar compilado de la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Exponemos el puerto
EXPOSE 8080
# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]