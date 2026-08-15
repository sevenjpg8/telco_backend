
CREATE TABLE public.usuario (
    id bigint NOT NULL,
    username character varying(50) NOT NULL,
    password_hash character varying(255) NOT NULL,
    rol character varying(20) NOT NULL,
    supervisor_id bigint,
    activo boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE public.usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.usuario_id_seq OWNED BY public.usuario.id;

CREATE TABLE public.venta (
    id bigint NOT NULL,
    agente_id bigint NOT NULL,
    dni_cliente character varying(11) NOT NULL,
    nombre_cliente character varying(100) NOT NULL,
    telefono_cliente character varying(9) NOT NULL,
    direccion_cliente character varying(200) NOT NULL,
    plan_actual character varying(100) NOT NULL,
    plan_nuevo character varying(100) NOT NULL,
    codigo_llamada character varying(50) NOT NULL,
    producto character varying(50) NOT NULL,
    monto numeric(10,2) NOT NULL,
    estado character varying(20) NOT NULL,
    motivo_rechazo character varying(500),
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_validacion timestamp without time zone,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP CONSTRAINT venta_update_at_not_null NOT NULL
);

CREATE SEQUENCE public.venta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.venta_id_seq OWNED BY public.venta.id;

ALTER TABLE ONLY public.usuario ALTER COLUMN id SET DEFAULT nextval('public.usuario_id_seq'::regclass);

ALTER TABLE ONLY public.venta ALTER COLUMN id SET DEFAULT nextval('public.venta_id_seq'::regclass);

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT uq_venta_codigo_llamada UNIQUE (codigo_llamada);

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_supervisor FOREIGN KEY (supervisor_id) REFERENCES public.usuario(id);

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_agente FOREIGN KEY (agente_id) REFERENCES public.usuario(id);


