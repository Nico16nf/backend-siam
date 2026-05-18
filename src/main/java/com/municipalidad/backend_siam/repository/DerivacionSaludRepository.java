package com.municipalidad.backend_siam.repository;

import com.municipalidad.backend_siam.entity.adulto_mayor.DerivacionSalud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DerivacionSaludRepository extends JpaRepository<DerivacionSalud, Long> {

    // 🔷 LISTAR POR ESTADO CON ADULTO MAYOR
    @Query("""
        SELECT d FROM DerivacionSalud d
        JOIN FETCH d.adultoMayor a
        WHERE d.estado = :estado
    """)
    List<DerivacionSalud> findByEstadoConAdulto(String estado);

    // 🔷 LISTAR TODAS CON ADULTO MAYOR
    @Query("""
        SELECT d FROM DerivacionSalud d
        JOIN FETCH d.adultoMayor a
    """)
    List<DerivacionSalud> findAllConAdulto();

    // 🔷 LISTAR POR ADULTO MAYOR
    @Query("""
        SELECT d FROM DerivacionSalud d
        JOIN FETCH d.adultoMayor a
        WHERE a.id = :adultoMayorId
    """)
    List<DerivacionSalud> findByAdultoMayorId(Long adultoMayorId);
}