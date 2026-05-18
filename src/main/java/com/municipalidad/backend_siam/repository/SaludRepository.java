package com.municipalidad.backend_siam.repository;

import com.municipalidad.backend_siam.entity.adulto_mayor.Salud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaludRepository extends JpaRepository<Salud, Long> {

    // LISTAR TODAS LAS ATENCIONES CON ADULTO MAYOR
    @Query("""
        SELECT s FROM Salud s
        JOIN FETCH s.adultoMayor a
        ORDER BY s.fechaAtencion DESC
    """)
    List<Salud> findAllConAdulto();

    // LISTAR HISTORIAL DE UN ADULTO MAYOR
    @Query("""
        SELECT s FROM Salud s
        JOIN FETCH s.adultoMayor a
        WHERE a.id = :adultoMayorId
        ORDER BY s.fechaAtencion DESC
    """)
    List<Salud> findByAdultoMayorIdConAdulto(Long adultoMayorId);

    // BUSCAR POR MÉDICO
    @Query("""
        SELECT s FROM Salud s
        JOIN FETCH s.adultoMayor a
        WHERE LOWER(s.medicoResponsable) LIKE LOWER(CONCAT('%', :medico, '%'))
        ORDER BY s.fechaAtencion DESC
    """)
    List<Salud> buscarPorMedico(String medico);

    // BUSCAR POR TIPO DE ATENCIÓN
    @Query("""
        SELECT s FROM Salud s
        JOIN FETCH s.adultoMayor a
        WHERE s.tipoAtencion = :tipoAtencion
        ORDER BY s.fechaAtencion DESC
    """)
    List<Salud> buscarPorTipoAtencion(String tipoAtencion);
}