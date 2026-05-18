package com.municipalidad.backend_siam.repository;

import com.municipalidad.backend_siam.entity.adulto_mayor.Pension65;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Pension65Repository extends JpaRepository<Pension65, Long> {

    Optional<Pension65> findByAdultoMayorId(Long adultoMayorId);

    boolean existsByAdultoMayorId(Long adultoMayorId);

    List<Pension65> findByBeneficiarioTrue();

    List<Pension65> findByPosibleBeneficiarioTrue();

    List<Pension65> findByEstadoIgnoreCase(String estado);
}