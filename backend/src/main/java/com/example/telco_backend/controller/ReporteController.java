package com.example.telco_backend.controller;

import com.example.telco_backend.dto.ResumenResponse;
import com.example.telco_backend.service.ReporteService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/resumen")
    public ResponseEntity<ResumenResponse> getResumen(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            Authentication authentication) {

        String username = authentication.getName();

        if (username == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        ResumenResponse resumen = reporteService.getResumen(
                username,
                desde,
                hasta);

        return ResponseEntity.ok(resumen);
    }
}