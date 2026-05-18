package com.municipalidad.backend_siam.repository;

import com.municipalidad.backend_siam.entity.Usuarios;
import com.municipalidad.backend_siam.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {

    // 🔐 LOGIN
    Optional<Usuarios> findByUsuario(String usuario);

    Optional<Usuarios> findByUsuarioAndActivoTrue(String usuario);

    // 🔥 LOGIN CON ROL (CLAVE PARA TU CASO)
    Optional<Usuarios> findByUsuarioAndRol(String usuario, Rol rol);

    // 📧 VALIDACIONES
    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    // 🔍 BÚSQUEDAS
    Optional<Usuarios> findByEmail(String email);

    Optional<Usuarios> findByDni(String dni);

    List<Usuarios> findByRol(Rol rol);

    List<Usuarios> findByActivoTrue();

}