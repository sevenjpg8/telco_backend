package com.example.telco_backend.repository;

import com.example.telco_backend.entity.EstadoVenta;
import com.example.telco_backend.entity.Usuario;
import com.example.telco_backend.entity.Venta;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {

    Page<Venta> findByAgenteId(Usuario agente, Pageable pageable);

    Page<Venta> findByAgenteIdAndEstado(Usuario agente, EstadoVenta estado, Pageable pageable);

    Page<Venta> findByAgenteIdAndFechaRegistroBetween(Usuario agente, LocalDateTime desde, LocalDateTime hasta,
            Pageable pageable);

    Page<Venta> findByAgenteIdAndEstadoAndFechaRegistroBetween(Usuario agente, EstadoVenta estado,
            LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<Venta> findByEstado(EstadoVenta estado, Pageable pageable);

    long countByEstadoAndFechaRegistroBetween(
            EstadoVenta estado,
            LocalDateTime desde,
            LocalDateTime hasta);

    @Query("""
            SELECT COALESCE(SUM(v.monto), 0)
            FROM Venta v
            WHERE v.estado = :estado
            AND v.fechaRegistro BETWEEN :desde AND :hasta
            """)
    BigDecimal sumMontoByEstadoAndFecha(
            @Param("estado") EstadoVenta estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    List<Venta> findByFechaRegistroBetween(LocalDateTime desde, LocalDateTime hasta);

}
