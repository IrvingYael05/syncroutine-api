# SyncRoutine API Server

El servicio core de SyncRoutine. Esta API RESTful gestiona la lógica de negocio, el registro histórico de métricas de rendimiento y actúa como el puente de autorización y seguridad centralizado para el ecosistema de dispositivos (Web, Wearables y Smart TVs).

## Arquitectura y Capacidades

- **IoT Device Authorization:** Implementación de un flujo de vinculación segura basado en códigos temporales (PIN) de 6 dígitos con caducidad lógica (10 minutos), diseñado para pantallas con interfaces limitadas.
- **Agregación de Métricas:** Procesamiento y cálculo en tiempo real de desviaciones de tiempo, eficiencia de rutinas y rachas de usuarios a través del historial de actividades.
- **Limpieza Automática (Cron Jobs):** Procesos programados en segundo plano para la depuración y eliminación física de tokens y solicitudes de vinculación expiradas.
- **Stateless Security:** Arquitectura sin estado validando firmas asimétricas JWT (ES256) contra el proveedor de identidades mediante JWKS (_JSON Web Key Sets_).

## Stack Tecnológico

- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3.x
- **Persistencia:** Spring Data JPA / Hibernate
- **Base de Datos:** PostgreSQL (Alojamiento en Supabase)
- **Seguridad:** Spring Security (OAuth2 Resource Server)

## Configuración y Ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/IrvingYael05/syncroutine-api.git
   cd syncroutine-api
   ```

2. Configura las variables de entorno. Duplica application.example.properties hacia application.properties y define los secretos:

    ```Properties
    spring.datasource.url=jdbc:postgresql://TU_DB_HOST:5432/postgres
    spring.datasource.username=postgres
    spring.datasource.password=TU_PASSWORD
    supabase.jwt.secret=TU_JWT_SECRET
    supabase.url=TU_SUPABASE_URL
    ```

3. Compila y ejecuta la aplicación usando Maven Wrapper:

    ``` Bash
    ./mvnw spring-boot:run
    ```