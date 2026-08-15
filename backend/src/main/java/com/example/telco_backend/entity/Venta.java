package com.example.telco_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private Usuario agenteId;

    @Column(name = "dni_cliente", nullable = false, length = 11)
    private String dniCliente;

    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    @Column(name = "telefono_cliente", nullable = false, length = 9)
    private String telefonoCliente;

    @Column(name = "direccion_cliente", nullable = false, length = 200)
    private String direccionCliente;

    @Column(name = "plan_actual", nullable = false, length = 100)
    private String planActual;

    @Column(name = "plan_nuevo", nullable = false, length = 100)
    private String planNuevo;

    @Column(name = "codigo_llamada", nullable = false, unique = true, length = 50)
    private String codigoLlamada;

    @Column(name = "producto", nullable = false, length = 50)
    private String producto;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoVenta estado;

    @Column(name = "motivo_rechazo", length = 500)
    private String motivoRechazo;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
