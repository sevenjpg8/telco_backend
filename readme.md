# Telco Backend

API REST para la gestión del flujo de ventas de servicios **Telco Fija Hogar**, desarrollada con Java 21, Spring Boot 3 y PostgreSQL.

El proyecto incluye:

* Autenticación mediante JWT.
* Control de acceso por roles.
* Registro de ventas.
* Aprobación y rechazo de ventas.
* Consulta de ventas por agente.
* Consulta de ventas por equipo.
* Filtros por estado y fechas.
* Reportes y resumen de ventas.
* Frontend mínimo para demostrar el funcionamiento.
* Documentación OpenAPI.

---

# 1. Tecnologías utilizadas

## Backend

* Java 21
* Spring Boot 3
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* Maven Wrapper

## Base de datos

* PostgreSQL

## Frontend

* Node.js
* npm
* JavaScript / HTML / CSS

---

# 2. Requisitos previos

Antes de ejecutar el proyecto se debe tener instalado:

* Java 21
* PostgreSQL
* Node.js y npm
* Git
* Navegador web

**No es necesario instalar Maven manualmente**, ya que el proyecto incluye Maven Wrapper.

Para verificar las instalaciones:

### Windows

Abrir PowerShell o CMD:

```bash
java -version
psql --version
node -v
npm -v
git --version
```

### Linux

Abrir una terminal:

```bash
java -version
psql --version
node -v
npm -v
git --version
```

---

# 3. Clonar el repositorio

Clonar el proyecto:

```bash
git clone <URL_DEL_REPOSITORIO>
```

Ingresar a la carpeta:

```bash
cd <NOMBRE_DEL_PROYECTO>
```

La estructura principal debe ser similar a:

```text
telco-project/
├── .mvn/
├── mvnw
├── mvnw.cmd
├── backend/
├── frontend/
├── docs/
└── README.md
```

---

# 4. Configuración de la base de datos

## 4.1 Crear la base de datos

Ingresar a PostgreSQL y crear la base de datos:

```sql
CREATE DATABASE pruebaBD;
```

También puede realizarse desde la terminal:

```bash
createdb pruebaBD
```

En Windows, si `createdb` no está disponible directamente, se puede ejecutar el comando desde **SQL Shell (psql)** o mediante **pgAdmin**.

---

## 4.2 Ejecutar schema.sql

El archivo:

```text
backend/src/main/resources/schema.sql
```

contiene la estructura de la base de datos.

Este archivo crea:

* Tabla `usuario`.
* Tabla `venta`.
* Secuencias.
* Claves primarias.
* Claves foráneas.
* Restricción de código de llamada único.

Ejecutar:

```bash
psql -U postgres -d pruebaBD -f backend/src/main/resources/schema.sql
```

También se puede ejecutar el archivo desde pgAdmin utilizando el **Query Tool**.

---

## 4.3 Ejecutar data.sql

Después de ejecutar `schema.sql`, ejecutar:

```bash
psql -U postgres -d pruebaBD -f backend/src/main/resources/data.sql
```

Este archivo contiene los datos iniciales necesarios para probar la aplicación.

Incluye usuarios de prueba y ventas con diferentes estados:

* `PENDIENTE`
* `APROBADA`
* `RECHAZADA`

> **Importante:** ejecutar siempre `schema.sql` antes de `data.sql`.

---

# 5. Configuración del backend

El archivo de configuración se encuentra en:

```text
backend/src/main/resources/application.properties
```

Configurar los datos de conexión a PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pruebaBD
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

jwt.secret=CLAVE_SECRETA_JWT
jwt.expiration=86400000
```
> **Nota de seguridad:** Para facilitar la ejecución local de la prueba técnica, estos valores se configuran directamente en `application.properties`. En un entorno de producción se recomienda externalizar las credenciales y la clave JWT mediante variables de entorno o un sistema de gestión de secretos.

## Variables de configuración

| Variable                     | Descripción                                    | Ejemplo                                     |
| ---------------------------- | ---------------------------------------------- | ------------------------------------------- |
| `spring.datasource.url`      | URL de conexión a PostgreSQL                   | `jdbc:postgresql://localhost:5432/pruebaBD` |
| `spring.datasource.username` | Usuario de PostgreSQL                          | `postgres`                                  |
| `spring.datasource.password` | Contraseña de PostgreSQL                       | `TU_PASSWORD`                               |
| `jwt.secret`                 | Clave utilizada para firmar los JWT            | `CLAVE_SECRETA_JWT`                         |
| `jwt.expiration`             | Tiempo de expiración del token en milisegundos | `86400000`                                  |

La contraseña de PostgreSQL y la clave JWT deben configurarse de acuerdo con el entorno local.

---

# 6. Puertos utilizados

| Servicio   | Puerto | URL                            |
| ---------- | -----: | ------------------------------ |
| Backend    | `8080` | `http://localhost:8080`        |
| API        | `8080` | `http://localhost:8080/api/v1` |
| Frontend   | `5173` | `http://localhost:5173`        |
| PostgreSQL | `5432` | `localhost:5432`               |

El backend tiene CORS configurado para permitir solicitudes provenientes de:

```text
http://localhost:5173
```

---

# 7. Ejecutar el backend

El proyecto incluye **Maven Wrapper**, por lo que no es necesario instalar Maven ni configurar la variable `PATH`.

Desde la **raíz del proyecto**:

### Windows

```powershell
.\mvnw.cmd -f backend/pom.xml spring-boot:run
```

### Linux

```bash
./mvnw -f backend/pom.xml spring-boot:run
```

También es posible ejecutar el backend directamente desde **Spring Boot Dashboard** en Visual Studio Code.

El backend estará disponible en:

```text
http://localhost:8080
```

La API utiliza el prefijo:

```text
/api/v1
```

Por ejemplo, el endpoint de autenticación es:

```text
POST http://localhost:8080/api/v1/auth/login
```

---

# 8. Ejecutar el frontend

## 8.1 Configurar variables de entorno

El frontend utiliza una variable de entorno para definir la URL base de la API.

Dentro de la carpeta `frontend`, crear un archivo llamado:

```text
.env
```
Agregar:

```text
VITE_API_URL=http://localhost:8080/api/v1
```
La estructura debe quedar:

```text
frontend/
├── src/
├── package.json
├── .env
└── ...
```
> **Importante:** El archivo .env está incluido en .gitignore y no forma parte del repositorio. Por ello, debe crearse manualmente al configurar el proyecto localmente.

## 8.2 Configurar variables de entorno

Abrir otra terminal e ingresar a:

```bash
cd frontend
```

Instalar las dependencias:

```bash
npm install
```

## 8.3 Configurar variables de entorno

Ejecutar el servidor de desarrollo:

```bash
npm run dev
```

El frontend estará disponible en:

```text
http://localhost:5173
```

---

# 9. Usuarios de prueba

La base de datos incluye los siguientes usuarios:

| Usuario       | Contraseña   | Rol        |
| ------------- | ------------ | ---------- |
| `admin`       | `Admin*123`  | ADMIN      |
| `agente1`     | `Agente*123` | AGENTE     |
| `back1`       | `Back*123`   | BACKOFFICE |
| `supervisor1` | `Sup*123`    | SUPERVISOR |

Estos usuarios permiten probar los diferentes permisos y flujos de la aplicación.

---

# 10. Flujo funcional

## Agente

El agente puede:

1. Iniciar sesión.
2. Registrar una venta.
3. Consultar sus propias ventas.
4. Filtrar sus ventas por estado y rango de fechas.

Las nuevas ventas se registran inicialmente como:

```text
PENDIENTE
```

---

## Backoffice

El usuario de backoffice puede:

1. Iniciar sesión.
2. Consultar ventas pendientes.
3. Aprobar una venta.
4. Rechazar una venta indicando un motivo.

Cuando una venta es aprobada o rechazada, se registra la fecha de validación.

---

## Supervisor

El supervisor puede:

1. Consultar las ventas de sus agentes.
2. Filtrar por estado.
3. Filtrar por agente.
4. Filtrar por rango de fechas.
5. Consultar el resumen de ventas.

El resumen incluye:

* Conteo de ventas pendientes.
* Conteo de ventas aprobadas.
* Conteo de ventas rechazadas.
* Monto total de ventas aprobadas.
* Serie de ventas por día.

---

# 11. Endpoints principales

La API está disponible bajo:

```text
/api/v1
```

### Autenticación

```text
POST /auth/login
```

### Agente

```text
POST /ventas
GET  /ventas/mis-ventas
```

### Backoffice

```text
GET  /ventas/pendientes
POST /ventas/{id}/aprobar
POST /ventas/{id}/rechazar
```

### Supervisor

```text
GET /ventas/equipo
GET /reportes/resumen
```

Las operaciones protegidas requieren un token JWT:

```text
Authorization: Bearer <TOKEN>
```

---

# 12. Documentación de la API

La API cuenta con documentación mediante **OpenAPI**.

La especificación OpenAPI se encuentra en:

```text
docs/
├── openapi.yaml
└── index.html
```

La documentación interactiva está disponible mediante **Swagger UI** en GitHub Pages:

**[Ver documentación interactiva de la API](https://sevenjpg8.github.io/telco_backend/)**

La documentación incluye:

* Endpoints.
* Parámetros.
* Request bodies.
* Respuestas.
* Códigos HTTP.
* Esquemas de datos.
* Autenticación mediante JWT.

La especificación `openapi.yaml` también puede utilizarse para importar la API en herramientas compatibles con OpenAPI.

---

# 13. Documentación del proyecto

La documentación complementaria se encuentra en la carpeta `docs/`.

### Diagrama de la solución

**[Ver diagrama de la solución](https://sevenjpg8.github.io/telco_backend/diagrama-solucion)**

Incluye una representación de la arquitectura y el flujo general entre frontend, backend, seguridad, servicios y base de datos.

### Decisiones técnicas y guía de despliegue

**[Ver decisiones técnicas y guía de despliegue](https://sevenjpg8.github.io/telco_backend/decisiones-tecnicas)**

Documenta las principales decisiones técnicas del proyecto y los pasos necesarios para ejecutar la aplicación localmente.

La estructura de documentación es:

```text
docs/
├── diagrama-solucion.md
├── decisiones-tecnicas.md
├── openapi.yaml
└── index.html
```

---

# 14. Estructura del proyecto

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
│   └── openapi.yaml
│
└── README.md
```

---

# 15. Orden recomendado de ejecución

Para ejecutar el proyecto desde cero:

```text
1. Instalar Java 21, PostgreSQL y Node.js.
2. Clonar el repositorio.
3. Crear la base de datos pruebaBD.
4. Ejecutar schema.sql.
5. Ejecutar data.sql.
6. Configurar application.properties.
7. Ejecutar el backend mediante Maven Wrapper o Spring Boot Dashboard.
8. Instalar las dependencias del frontend.
9. Ejecutar el frontend.
10. Iniciar sesión con uno de los usuarios de prueba.
```

Con estos pasos se puede ejecutar localmente el proyecto completo y probar el flujo de ventas Telco.
