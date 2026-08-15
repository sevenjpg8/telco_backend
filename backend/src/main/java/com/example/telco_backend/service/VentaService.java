package com.example.telco_backend.service;

import com.example.telco_backend.entity.Venta;
import com.example.telco_backend.exception.RecursoNoEncontradoException;
import com.example.telco_backend.exception.ReglaNegocioException;
import com.example.telco_backend.entity.EstadoVenta;
import com.example.telco_backend.dto.VentaRequest;
import com.example.telco_backend.repository.VentaRepository;
import com.example.telco_backend.entity.Usuario;
import com.example.telco_backend.repository.UsuarioRepository;
import com.example.telco_backend.specification.VentaSpecification;

import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;

    public VentaService(VentaRepository ventaRepository, UsuarioRepository usuarioRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Venta createVenta(VentaRequest ventaRequest, String username) {
        Usuario agenteId = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = new Venta();

        venta.setAgenteId(agenteId);
        venta.setDniCliente(ventaRequest.getDniCliente());
        venta.setNombreCliente(ventaRequest.getNombreCliente());
        venta.setTelefonoCliente(ventaRequest.getTelefonoCliente());
        venta.setDireccionCliente(ventaRequest.getDireccionCliente());
        venta.setPlanActual(ventaRequest.getPlanActual());
        venta.setPlanNuevo(ventaRequest.getPlanNuevo());
        venta.setCodigoLlamada(ventaRequest.getCodigoLlamada());
        venta.setProducto(ventaRequest.getProducto());
        venta.setMonto(ventaRequest.getMonto());

        venta.setEstado(EstadoVenta.PENDIENTE); // Establecer el estado inicial como "PENDIENTE"

        venta.setFechaRegistro(LocalDateTime.now());
        venta.setCreatedAt(LocalDateTime.now());
        venta.setUpdatedAt(LocalDateTime.now());

        return ventaRepository.save(venta);
    }

    public Page<Venta> getMisVentas(
            String username,
            EstadoVenta estado,
            LocalDate desde,
            LocalDate hasta,
            Pageable pageable) {

        Usuario agente = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDateTime fechaDesde = null;
        LocalDateTime fechaHasta = null;

        if (desde != null) {
            fechaDesde = desde.atStartOfDay();
        }

        if (hasta != null) {
            fechaHasta = hasta.atTime(23, 59, 59);
        }

        if (estado != null && fechaDesde != null && fechaHasta != null) {
            return ventaRepository.findByAgenteIdAndEstadoAndFechaRegistroBetween(
                    agente, estado, fechaDesde, fechaHasta, pageable);
        }

        if (estado != null) {
            return ventaRepository.findByAgenteIdAndEstado(
                    agente, estado, pageable);
        }

        if (fechaDesde != null && fechaHasta != null) {
            return ventaRepository.findByAgenteIdAndFechaRegistroBetween(
                    agente, fechaDesde, fechaHasta, pageable);
        }

        return ventaRepository.findByAgenteId(agente, pageable);
    }

    public Page<Venta> getVentasPendientes(Pageable pageable) {
        return ventaRepository.findByEstado(EstadoVenta.PENDIENTE, pageable);
    }

    public Venta aprobarVenta(@NonNull Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada"));

        if (venta.getEstado() != EstadoVenta.PENDIENTE) {
            throw new ReglaNegocioException("Solo se pueden aprobar ventas pendientes");
        }

        venta.setEstado(EstadoVenta.APROBADA);
        venta.setFechaValidacion(LocalDateTime.now());
        venta.setUpdatedAt(LocalDateTime.now());

        return ventaRepository.save(venta);
    }

    public Venta rechazarVenta(@NonNull Long id, @Nullable String motivoRechazo) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada"));

        if (venta.getEstado() != EstadoVenta.PENDIENTE) {
            throw new ReglaNegocioException("Solo se pueden rechazar ventas pendientes");
        }

        if (motivoRechazo == null || motivoRechazo.trim().isEmpty()) {
            throw new ReglaNegocioException("El motivo de rechazo no puede estar vacío");
        }

        venta.setEstado(EstadoVenta.RECHAZADA);
        venta.setMotivoRechazo(motivoRechazo);
        venta.setFechaValidacion(LocalDateTime.now());
        venta.setUpdatedAt(LocalDateTime.now());

        return ventaRepository.save(venta);
    }

    public Page<Venta> getVentasEquipo(
            @NonNull String username,
            @Nullable EstadoVenta estado,
            @Nullable Long agenteId,
            @Nullable LocalDate desde,
            @Nullable LocalDate hasta,
            @NonNull Pageable pageable) {

        Usuario supervisor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDateTime fechaDesde = null;
        LocalDateTime fechaHasta = null;

        if (desde != null) {
            fechaDesde = desde.atStartOfDay();
        }

        if (hasta != null) {
            fechaHasta = hasta.atTime(23, 59, 59);
        }

        Specification<Venta> specification = VentaSpecification.porSupervisor(
                supervisor,
                estado,
                agenteId,
                fechaDesde,
                fechaHasta);

        return ventaRepository.findAll(specification, pageable);
    }
}
