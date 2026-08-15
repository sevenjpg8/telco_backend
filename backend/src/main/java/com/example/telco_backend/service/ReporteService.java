package com.example.telco_backend.service;

import com.example.telco_backend.dto.ResumenResponse;
import com.example.telco_backend.dto.VentaPorDiaResponse;
import com.example.telco_backend.entity.EstadoVenta;
import com.example.telco_backend.entity.Usuario;
import com.example.telco_backend.entity.Venta;
import com.example.telco_backend.repository.UsuarioRepository;
import com.example.telco_backend.repository.VentaRepository;
import com.example.telco_backend.specification.VentaSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteService(
            VentaRepository ventaRepository,
            UsuarioRepository usuarioRepository) {

        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ResumenResponse getResumen(
            String username,
            LocalDate desde,
            LocalDate hasta) {

        Usuario supervisor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDateTime fechaDesde = desde.atStartOfDay();
        LocalDateTime fechaHasta = hasta.atTime(23, 59, 59);

        Specification<Venta> specification =
                VentaSpecification.reporteSupervisor(
                        supervisor,
                        fechaDesde,
                        fechaHasta
                );

        List<Venta> ventas = ventaRepository.findAll(specification);

        long pendientes = ventas.stream()
                .filter(v -> v.getEstado() == EstadoVenta.PENDIENTE)
                .count();

        long aprobadas = ventas.stream()
                .filter(v -> v.getEstado() == EstadoVenta.APROBADA)
                .count();

        long rechazadas = ventas.stream()
                .filter(v -> v.getEstado() == EstadoVenta.RECHAZADA)
                .count();

        BigDecimal montoTotalAprobadas = ventas.stream()
                .filter(v -> v.getEstado() == EstadoVenta.APROBADA)
                .map(v -> v.getMonto() != null ? v.getMonto() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO,
                        (acumulado, monto) -> acumulado.add(monto != null ? monto : BigDecimal.ZERO));

        Map<LocalDate, List<Venta>> ventasAgrupadas =
                ventas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getFechaRegistro().toLocalDate()
                        ));

        List<VentaPorDiaResponse> ventasPorDia =
                ventasAgrupadas.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(entry -> {

                            LocalDate fecha = entry.getKey();
                            List<Venta> ventasDia = entry.getValue();

                            long cantidad = ventasDia.size();

                            BigDecimal monto = ventasDia.stream()
                                    .map(v -> v.getMonto() != null ? v.getMonto() : BigDecimal.ZERO)
                                    .reduce(BigDecimal.ZERO, (acumulado, monto2) -> acumulado.add(monto2 != null ? monto2 : BigDecimal.ZERO));

                            return new VentaPorDiaResponse(
                                    fecha.toString(),
                                    cantidad,
                                    monto
                            );
                        })
                        .toList();

        ResumenResponse response = new ResumenResponse();

        response.setPendientes(pendientes);
        response.setAprobadas(aprobadas);
        response.setRechazadas(rechazadas);
        response.setMontoTotalAprobadas(montoTotalAprobadas);
        response.setVentasPorDia(ventasPorDia);

        return response;
    }
}
