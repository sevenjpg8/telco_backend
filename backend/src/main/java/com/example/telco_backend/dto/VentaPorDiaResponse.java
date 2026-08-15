package com.example.telco_backend.dto;

import java.math.BigDecimal;

public class VentaPorDiaResponse {

    private String fecha;
    private Long cantidad;
    private BigDecimal monto;

    public VentaPorDiaResponse() {
    }

    public VentaPorDiaResponse(String fecha, Long cantidad, BigDecimal monto) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
