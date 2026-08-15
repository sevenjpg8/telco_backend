package com.example.telco_backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class ResumenResponse {

    private Long pendientes;
    private Long aprobadas;
    private Long rechazadas;

    private BigDecimal montoTotalAprobadas;

    private List<VentaPorDiaResponse> ventasPorDia;

    public ResumenResponse() {
    }

    public Long getPendientes() {
        return pendientes;
    }

    public void setPendientes(Long pendientes) {
        this.pendientes = pendientes;
    }

    public Long getAprobadas() {
        return aprobadas;
    }

    public void setAprobadas(Long aprobadas) {
        this.aprobadas = aprobadas;
    }

    public Long getRechazadas() {
        return rechazadas;
    }

    public void setRechazadas(Long rechazadas) {
        this.rechazadas = rechazadas;
    }

    public BigDecimal getMontoTotalAprobadas() {
        return montoTotalAprobadas;
    }

    public void setMontoTotalAprobadas(BigDecimal montoTotalAprobadas) {
        this.montoTotalAprobadas = montoTotalAprobadas;
    }

    public List<VentaPorDiaResponse> getVentasPorDia() {
        return ventasPorDia;
    }

    public void setVentasPorDia(List<VentaPorDiaResponse> ventasPorDia) {
        this.ventasPorDia = ventasPorDia;
    }
}