package com.example.telco_backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VentaRequest {

    @NotBlank
    @Pattern(regexp = "\\d{8,11}", message = "El DNI debe tener entre 8 y 11 dígitos")
    private String dniCliente;

    @NotBlank
    private String nombreCliente;

    @NotBlank
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String telefonoCliente;

    @NotBlank
    private String direccionCliente;

    @NotBlank
    private String planActual;

    @NotBlank
    private String planNuevo;

    @NotBlank
    private String codigoLlamada;

    @NotBlank
    private String producto;

    @NotNull
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getPlanActual() {
        return planActual;
    }

    public void setPlanActual(String planActual) {
        this.planActual = planActual;
    }

    public String getPlanNuevo() {
        return planNuevo;
    }

    public void setPlanNuevo(String planNuevo) {
        this.planNuevo = planNuevo;
    }

    public String getCodigoLlamada() {
        return codigoLlamada;
    }

    public void setCodigoLlamada(String codigoLlamada) {
        this.codigoLlamada = codigoLlamada;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
