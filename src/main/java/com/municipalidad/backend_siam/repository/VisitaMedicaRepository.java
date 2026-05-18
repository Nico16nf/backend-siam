package com.municipalidad.backend_siam.repository;

import com.municipalidad.backend_siam.entity.adulto_mayor.VisitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitaMedicaRepository extends JpaRepository<VisitaMedica, Long> {

    List<VisitaMedica> findByAdultoMayorId(Long adultoMayorId);

    List<VisitaMedica> findByRequiereSeguimientoTrue();

    List<VisitaMedica> findByRequiereReferenciaTrue();
}