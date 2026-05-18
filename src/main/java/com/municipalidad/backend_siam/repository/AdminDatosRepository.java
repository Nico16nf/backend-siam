package com.municipalidad.backend_siam.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AdminDatosRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Limpieza ejemplo: eliminar usuarios duplicados por DNI dejando el menor ID
    @Transactional
    public int limpiarDuplicadosUsuariosPorDni() {
        return entityManager.createNativeQuery("""
            DELETE FROM usuarios u
            WHERE u.id NOT IN (
                SELECT MIN(x.id)
                FROM usuarios x
                GROUP BY x.dni
            )
        """).executeUpdate();
    }
}