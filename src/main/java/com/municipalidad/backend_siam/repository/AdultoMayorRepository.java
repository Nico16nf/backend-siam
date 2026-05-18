package com.municipalidad.backend_siam.repository;

import com.municipalidad.backend_siam.entity.adulto_mayor.AdultoMayor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdultoMayorRepository extends JpaRepository<AdultoMayor, Long> {

    List<AdultoMayor> findByActivoTrue();

    Optional<AdultoMayor> findByDatosPersonalesDni(String dni);
}