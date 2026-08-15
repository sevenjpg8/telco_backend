INSERT INTO usuario (
    username,
    password_hash,
    rol,
    supervisor_id,
    activo,
    created_at,
    updated_at
)
VALUES (
    'admin',
    '$2a$10$6wBdwLofWdP/kNHkk3e3UedurS756Dk2X51DMig5xEbgdzf.spA46',
    'ADMIN',
    NULL,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- SUPERVISOR
INSERT INTO usuario (
    username,
    password_hash,
    rol,
    supervisor_id,
    activo,
    created_at,
    updated_at
)
VALUES (
    'supervisor1',
    '$2a$10$.QVPbSxgxIUF5sah1h9kLOmKxfNCF9oDxfBENsO7rxgmpwooRrYWO',
    'SUPERVISOR',
    NULL,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- AGENTE
INSERT INTO usuario (
    username,
    password_hash,
    rol,
    supervisor_id,
    activo,
    created_at,
    updated_at
)
VALUES (
    'agente1',
    '$2a$10$2wKRz/Q7r5GAd/qtF3KQDelheDAnxJK59XwsOtJIUGSkoPcD59/wK',
    'AGENTE',
    (SELECT id FROM usuario WHERE username = 'supervisor1'),
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- BACKOFFICE
INSERT INTO usuario (
    username,
    password_hash,
    rol,
    supervisor_id,
    activo,
    created_at,
    updated_at
)
VALUES (
    'back1',
    '$2a$10$lnFExFxK/tQxs89XcwEShOLm9FRve7TtxFnpBvIBUPdOMu5uU5WCm',
    'BACKOFFICE',
    NULL,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- =========================================
-- VENTAS
-- =========================================

INSERT INTO public.venta
    (
        agente_id,
        dni_cliente,
        nombre_cliente,
        telefono_cliente,
        direccion_cliente,
        plan_actual,
        plan_nuevo,
        codigo_llamada,
        producto,
        monto,
        estado,
        motivo_rechazo,
        fecha_registro,
        fecha_validacion
    )
VALUES

-- 1. PENDIENTE
(
    2,
    '74859632',
    'Carlos Ramirez',
    '987654321',
    'Av. Brasil 123',
    'Internet 300 Mbps',
    'Internet 600 Mbps',
    'CALL-001',
    'FIJA_HOGAR',
    129.90,
    'PENDIENTE',
    NULL,
    '2026-08-08 09:15:00',
    NULL
),

-- 2. APROBADA
(
    2,
    '45217896',
    'Maria Torres',
    '986543210',
    'Av. Arequipa 456',
    'Internet 300 Mbps',
    'Internet 600 Mbps',
    'CALL-002',
    'FIJA_HOGAR',
    119.90,
    'APROBADA',
    NULL,
    '2026-08-09 10:30:00',
    '2026-08-09 14:20:00'
),

-- 3. RECHAZADA
(
    2,
    '60984521',
    'Jose Mendoza',
    '985432109',
    'Jr. Lima 789',
    'Internet 300 Mbps',
    'Internet 600 Mbps',
    'CALL-003',
    'FIJA_HOGAR',
    129.90,
    'RECHAZADA',
    'Datos del cliente no válidos',
    '2026-08-10 11:00:00',
    '2026-08-10 15:30:00'
),

-- 4. APROBADA
(
    2,
    '71826354',
    'Ana Rodriguez',
    '984321098',
    'Calle Los Olivos 321',
    'Internet 100 Mbps',
    'Internet 300 Mbps',
    'CALL-004',
    'FIJA_HOGAR',
    89.90,
    'APROBADA',
    NULL,
    '2026-08-11 09:45:00',
    '2026-08-11 13:10:00'
),

-- 5. RECHAZADA
(
    2,
    '39482716',
    'Luis Castillo',
    '983210987',
    'Av. Colonial 654',
    'Internet 300 Mbps',
    'Internet 600 Mbps',
    'CALL-005',
    'FIJA_HOGAR',
    129.90,
    'RECHAZADA',
    'Cliente no cumple las condiciones del plan',
    '2026-08-11 16:20:00',
    '2026-08-11 17:00:00'
),

-- 6. PENDIENTE
(
    2,
    '68152947',
    'Pedro Flores',
    '982109876',
    'Jr. Unión 987',
    'Internet 300 Mbps',
    'Internet 600 Mbps',
    'CALL-006',
    'FIJA_HOGAR',
    129.90,
    'PENDIENTE',
    NULL,
    '2026-08-12 10:15:00',
    NULL
),

-- 7. APROBADA
(
    2,
    '12345678',
    'Juan Perez',
    '981098765',
    'Av. La Marina 147',
    'Internet 100 Mbps',
    'Internet 300 Mbps',
    'CALL-007',
    'FIJA_HOGAR',
    89.90,
    'APROBADA',
    NULL,
    '2026-08-13 08:30:00',
    '2026-08-13 11:45:00'
),

-- 8. APROBADA
(
    2,
    '87654321',
    'Maria Lopez',
    '980987654',
    'Av. Javier Prado 258',
    'Internet 300 Mbps',
    'Internet 500 Mbps',
    'CALL-008',
    'FIJA_HOGAR',
    129.90,
    'APROBADA',
    NULL,
    '2026-08-13 12:00:00',
    '2026-08-13 15:20:00'
),

-- 9. PENDIENTE
(
    2,
    '52741683',
    'Sofia Vargas',
    '979876543',
    'Calle Las Flores 369',
    'Internet 300 Mbps',
    'Internet 600 Mbps',
    'CALL-009',
    'FIJA_HOGAR',
    119.90,
    'PENDIENTE',
    NULL,
    '2026-08-14 09:00:00',
    NULL
),

-- 10. APROBADA
(
    2,
    '61473829',
    'Diego Herrera',
    '978765432',
    'Jr. Los Pinos 741',
    'Internet 100 Mbps',
    'Internet 300 Mbps',
    'CALL-010',
    'FIJA_HOGAR',
    89.90,
    'APROBADA',
    NULL,
    '2026-08-14 10:30:00',
    '2026-08-14 12:15:00'
);