package com.example.telco_backend.specification;

import com.example.telco_backend.entity.Venta;
import com.example.telco_backend.entity.EstadoVenta;
import com.example.telco_backend.entity.Usuario;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaSpecification {

    public static Specification<Venta> porSupervisor(
            Usuario supervisorId,
            EstadoVenta estado,
            Long agenteId,
            LocalDateTime desde,
            LocalDateTime hasta) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // El agente de la venta debe pertenecer al supervisor
            predicates.add(
                    criteriaBuilder.equal(
                            root.get("agenteId").get("supervisorId"),
                            supervisorId));

            // Filtro por estado
            if (estado != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("estado"),
                                estado));
            }

            // Filtro por agente
            if (agenteId != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("agenteId").get("id"),
                                agenteId));
            }

            // Filtro por fecha inicial
            if (desde != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("fechaRegistro"),
                                desde));
            }

            // Filtro por fecha final
            if (hasta != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("fechaRegistro"),
                                hasta));
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Venta> reporteSupervisor(
        Usuario supervisor,
        LocalDateTime desde,
        LocalDateTime hasta) {

    return (root, query, criteriaBuilder) -> {

        List<Predicate> predicates = new ArrayList<>();

        // Solo ventas de agentes pertenecientes al supervisor
        predicates.add(
                criteriaBuilder.equal(
                        root.get("agenteId").get("supervisorId"),
                        supervisor
                )
        );

        // Fecha inicial
        if (desde != null) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("fechaRegistro"),
                            desde
                    )
            );
        }

        // Fecha final
        if (hasta != null) {
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("fechaRegistro"),
                            hasta
                    )
            );
        }

        return criteriaBuilder.and(
                predicates.toArray(new Predicate[0])
        );
    };
}
}
