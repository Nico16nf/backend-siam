package com.municipalidad.backend_siam.repository;

import com.municipalidad.backend_siam.entity.adulto_mayor.CampanaSalud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CampanaSaludRepository extends JpaRepository<CampanaSalud, Long> {

    List<CampanaSalud> findByEstado(String estado);

    List<CampanaSalud> findByTipoCampana(String tipoCampana);

    List<CampanaSalud> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);

    List<CampanaSalud> findByNombreCampanaContainingIgnoreCase(String nombreCampana);
}