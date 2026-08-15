flowchart TD

    A[Frontend<br>HTML + CSS + JavaScript]
    B[API REST<br>Spring Boot 3 + Java 21]
    C[Spring Security + JWT]
    D[Servicios<br>AuthService / VentaService / ReporteService]
    E[PostgreSQL]
    
    A -->|HTTP / JSON| B
    B --> C
    C --> D
    D --> E

    F[AGENTE]
    G[BACKOFFICE]
    H[SUPERVISOR]
    I[ADMIN]

    F --> A
    G --> A
    H --> A
    I --> A


# Diagrama de la solución

## Arquitectura

La solución utiliza una arquitectura cliente-servidor compuesta por:

- **Frontend:** HTML, CSS y JavaScript.
- **Backend:** Java 21 con Spring Boot 3.
- **Seguridad:** Spring Security y autenticación mediante JWT.
- **Persistencia:** PostgreSQL mediante Spring Data JPA / Hibernate.
- **Comunicación:** API REST utilizando JSON.

## Flujo general

1. El usuario accede al frontend e inicia sesión.
2. El backend valida las credenciales.
3. Si las credenciales son correctas, se genera un JWT con el rol del usuario.
4. El frontend almacena el token y lo envía en las peticiones protegidas.
5. Spring Security valida el JWT y determina el rol del usuario.
6. Según el rol, el usuario puede acceder a las operaciones correspondientes.
7. Los servicios del backend procesan las operaciones y consultan/modifican PostgreSQL.