package com.example.telco_backend.controller;

import com.example.telco_backend.entity.Venta;
import com.example.telco_backend.dto.VentaRequest;
import com.example.telco_backend.service.VentaService;
import com.example.telco_backend.entity.EstadoVenta;
import com.example.telco_backend.dto.RechazoRequest;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<Venta> createVenta(@Valid @RequestBody VentaRequest ventaRequest,
            Authentication authentication) {

        String username = authentication.getName();
        Venta createdVenta = ventaService.createVenta(ventaRequest, username);
        return ResponseEntity.ok(createdVenta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVenta(@PathVariable @NonNull Long id) {

        Venta venta = ventaService.getVentaById(id);

        return ResponseEntity.ok(venta);
    }

    @GetMapping("/mis-ventas")
    public ResponseEntity<Page<Venta>> getMisVentas(
            @RequestParam(required = false) EstadoVenta estado,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta,
            @PageableDefault(sort = "fechaRegistro", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {

        String username = authentication.getName();
        Page<Venta> ventas = ventaService.getMisVentas(username, estado, desde, hasta, pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<Page<Venta>> getVentasPendientes(Pageable pageable,
            Authentication authentication) {

        Page<Venta> ventas = ventaService.getVentasPendientes(pageable);
        return ResponseEntity.ok(ventas);
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<Venta> aprobarVenta(@PathVariable("id") long id) {

        Venta venta = ventaService.aprobarVenta(id);
        return ResponseEntity.ok(venta);
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<Venta> rechazarVenta(@PathVariable("id") long id,
            @RequestBody RechazoRequest rechazoRequest) {

        Venta venta = ventaService.rechazarVenta(id, rechazoRequest.getMotivoRechazo());
        return ResponseEntity.ok(venta);
    }

    @GetMapping("/equipo")
    public ResponseEntity<Page<Venta>> getVentasEquipo(
            @RequestParam(required = false) @Nullable EstadoVenta estado,
            @RequestParam(required = false) @Nullable Long agenteId,
            @RequestParam(required = false) @Nullable LocalDate desde,
            @RequestParam(required = false) @Nullable LocalDate hasta,
            @NonNull Pageable pageable,
            Authentication authentication) {

        String username = authentication.getName();

        if (username == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        Page<Venta> ventas = ventaService.getVentasEquipo(
                username,
                estado,
                agenteId,
                desde,
                hasta,
                pageable);

        return ResponseEntity.ok(ventas);
    }
}
