# Decisiones técnicas y guía de despliegue local

## 1. Decisiones técnicas

### Java 21

Se utilizó Java 21 como versión principal del backend, cumpliendo con el requisito de la prueba técnica y utilizando una versión LTS de Java.

### Spring Boot 3

Spring Boot 3 se utilizó para desarrollar la API REST. La aplicación se organizó mediante una arquitectura por capas, separando controladores, servicios, repositorios, entidades, DTOs y manejo de excepciones.

Esta estructura permite mantener una separación clara de responsabilidades y facilita el mantenimiento de la aplicación.

### Spring Security + JWT

La autenticación se implementó utilizando Spring Security y JSON Web Tokens (JWT).

El usuario inicia sesión mediante:

`POST /api/v1/auth/login`

Si las credenciales son correctas, el backend genera un token JWT que contiene el nombre de usuario y su rol.

Las operaciones protegidas requieren enviar el token mediante el encabezado:

`Authorization: Bearer <token>`

La aplicación utiliza una política de sesión `STATELESS`, por lo que el servidor no mantiene sesiones HTTP de los usuarios.

### Encriptación de contraseñas

Las contraseñas no se almacenan directamente en la base de datos. Se utiliza `BCryptPasswordEncoder` para almacenar un hash seguro de cada contraseña.

Durante el inicio de sesión, la contraseña proporcionada se compara contra el hash almacenado.

### Control de acceso por roles

Se implementó autorización basada en roles:

* **AGENTE:** registra ventas y consulta únicamente sus propias ventas.
* **BACKOFFICE:** consulta ventas pendientes y puede aprobar o rechazar ventas.
* **SUPERVISOR:** consulta las ventas de los agentes que tiene asignados y accede a los reportes.
* **ADMIN:** cuenta con acceso a las operaciones protegidas configuradas para administración.

La autorización se realiza en el backend, por lo que las restricciones no dependen únicamente del frontend.

### PostgreSQL

PostgreSQL se utilizó como sistema gestor de base de datos.

Las principales tablas utilizadas son:

* `usuario`
* `venta`

La tabla `venta` almacena un snapshot de los datos del cliente, como DNI, nombre, teléfono y dirección. De esta manera, la información registrada en una venta se conserva independientemente de cambios posteriores en los datos de un cliente.

Se utilizan claves primarias, claves foráneas y una restricción `UNIQUE` para garantizar la integridad de los datos y evitar códigos de llamada duplicados.

### Spring Data JPA

Se utilizó Spring Data JPA con Hibernate para realizar la persistencia de las entidades y facilitar el acceso a PostgreSQL mediante repositorios.

### Paginación y filtros

La consulta de ventas del agente utiliza paginación y ordenamiento para evitar cargar todos los registros simultáneamente.

También se implementaron filtros por estado y rango de fechas. Para las consultas del supervisor se permite adicionalmente filtrar por agente.

### Validaciones

Se implementaron validaciones para controlar los datos recibidos por la API, incluyendo:

* DNI de 8 u 11 dígitos.
* Teléfono de 9 dígitos.
* Campos obligatorios y no vacíos.
* Código de llamada único.
* Motivo obligatorio al rechazar una venta.
* Restricciones relacionadas con el estado de las ventas.

### Manejo global de errores

Se implementó un `GlobalExceptionHandler` mediante `@RestControllerAdvice`.

Los errores de la API se devuelven mediante una estructura JSON consistente que incluye:

* `timestamp`
* `path`
* `error`
* `message`

Además, se manejan errores de validación, reglas de negocio, recursos no encontrados y conflictos de integridad de datos.

### CORS

El backend permite solicitudes provenientes del frontend local:

`http://localhost:5173`

Esto permite la comunicación entre el frontend y la API durante el desarrollo local.

### Documentación de la API

La API se documentó mediante OpenAPI 3.0.3. La especificación se encuentra en el proyecto y permite consultar los endpoints, parámetros, cuerpos de las solicitudes, respuestas y esquema de autenticación JWT.

---

## 2. Estructura del proyecto

```text
telco-project/
│
├── .mvn/
│
├── mvnw
├── mvnw.cmd
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/example/telco_backend/
│   │       │       ├── config/
│   │       │       ├── controller/
│   │       │       ├── dto/
│   │       │       ├── entity/
│   │       │       ├── exception/
│   │       │       ├── repository/
│   │       │       └── service/
│   │       │
│   │       └── resources/
│   │           └── application.properties
│   │
│   ├── pom.xml
│   ├── schema.sql
│   └── data.sql
│
├── frontend/
│   ├── src/
│   └── package.json
│
├── docs/
│   ├── diagrama-solucion.md
│   ├── decisiones-tecnicas.md
│   ├── index.html
│   └── openapi.yaml
│
└── README.md
```

---

# 3. Guía de despliegue local

## 3.1 Requisitos

Para ejecutar el proyecto localmente se requiere:

* Java 21
* PostgreSQL
* Node.js y npm
* Navegador web

## 3.2 Configuración de PostgreSQL

Crear una base de datos para el proyecto:

```sql
CREATE DATABASE telcoBD;
```

Después de crear la base de datos, ejecutar los siguientes archivos en este orden:

1. `schema.sql` — crea las tablas, secuencias, claves primarias, claves foráneas y restricciones.
2. `data.sql` — inserta los usuarios y ventas iniciales.

El orden es importante porque `data.sql` depende de las tablas creadas previamente.

## 3.3 Configuración del backend

Configurar las credenciales de PostgreSQL y las propiedades de JWT en:

```text
backend/src/main/resources/application.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/telcoBD
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

jwt.secret=CLAVE_SECRETA_JWT
jwt.expiration=86400000
```

Los valores de contraseña y clave JWT deben adaptarse al entorno local.

La propiedad `spring.jpa.hibernate.ddl-auto=none` evita que Hibernate modifique automáticamente la estructura de la base de datos, ya que esta se encuentra definida mediante `schema.sql`.

> **Nota de seguridad:** Para facilitar la ejecución local de la prueba técnica, estos valores se configuran directamente en `application.properties`. En un entorno de producción se recomienda externalizar las credenciales y la clave JWT mediante variables de entorno o un sistema de gestión de secretos.

## 3.4 Ejecución del backend

Desde la carpeta `backend` ejecutar:

```bash
mvn spring-boot:run
```

El backend estará disponible en:

```text
http://localhost:8080
```

La API utiliza el prefijo:

```text
/api/v1
```

Por ejemplo:

```text
http://localhost:8080/api/v1/auth/login
```

## 3.5 Ejecución del frontend

Crear un archivo `.env` dentro de la carpeta `frontend`:
El frontend utiliza la variable de entorno `VITE_API_URL` para definir la URL base de la API backend.

```text
frontend/
├── .env
├── package.json
├── src/
└── ...
```
Contenido del archivo `.env`:
```text
VITE_API_URL=http://localhost:8080/api/v1
```

Desde la carpeta `frontend`, instalar las dependencias:

```bash
npm install
```

Luego ejecutar:

```bash
npm run dev
```

El frontend estará disponible normalmente en:

```text
http://localhost:5173
```

## 3.6 Usuarios de prueba

La base de datos incluye los siguientes usuarios:

| Usuario     | Contraseña | Rol        |
| ----------- | ---------- | ---------- |
| admin       | Admin*123  | ADMIN      |
| agente1     | Agente*123 | AGENTE     |
| back1       | Back*123   | BACKOFFICE |
| supervisor1 | Sup*123    | SUPERVISOR |

Estos usuarios permiten probar el flujo completo de autenticación y autorización.

## 3.7 Flujo de prueba

### Agente

El agente inicia sesión y registra una venta.

La venta se crea inicialmente con estado `PENDIENTE`. Posteriormente puede consultar únicamente sus propias ventas.

### Backoffice

El usuario de backoffice consulta las ventas pendientes y puede:

* Aprobar una venta.
* Rechazar una venta indicando un motivo.

Al validar una venta se registra la fecha de validación.

### Supervisor

El supervisor puede consultar las ventas realizadas por los agentes que tiene asignados, utilizando filtros por estado, agente y rango de fechas.

También puede consultar el resumen de ventas, que incluye:

* Cantidad de ventas pendientes.
* Cantidad de ventas aprobadas.
* Cantidad de ventas rechazadas.
* Monto total de ventas aprobadas.
* Serie de ventas por día.
