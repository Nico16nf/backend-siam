package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.entity.Rol;
import com.municipalidad.backend_siam.entity.Usuarios;
import com.municipalidad.backend_siam.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuariosService {

    private final UsuariosRepository repo;

    // =====================================================
    // 🔥 CREAR USUARIO
    // =====================================================
    public Usuarios crear(Usuarios usuario) {

        if (usuario == null) {
            throw new RuntimeException("Datos del usuario vacíos");
        }

        // 🔐 VALIDACIONES
        if (repo.existsByDni(usuario.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        if (repo.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (repo.existsByUsuario(usuario.getUsuario())) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (usuario.getRol() == null) {
            throw new RuntimeException("Debe asignar un rol");
        }

        // 🖼️ FOTO OPCIONAL
        // Angular debe mandar algo como: data:image/png;base64,iVBORw0KGgo...
        if (usuario.getFoto() != null && usuario.getFoto().isBlank()) {
            usuario.setFoto(null);
        }

        // 🔥 CONTROL DE NULL
        usuario.setActivo(usuario.getActivo() != null ? usuario.getActivo() : true);
        usuario.setBloqueado(usuario.getBloqueado() != null ? usuario.getBloqueado() : false);
        usuario.setIntentosFallidos(usuario.getIntentosFallidos() != null ? usuario.getIntentosFallidos() : 0);

        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(LocalDateTime.now());
        }

        // 🔐 RECOMENDADO
        // usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return repo.save(usuario);
    }

    // =====================================================
    // 📋 LISTAR
    // =====================================================
    public List<Usuarios> listar() {
        return repo.findAll();
    }

    public List<Usuarios> listarActivos() {
        return repo.findByActivoTrue();
    }

    public List<Usuarios> listarPorRol(Rol rol) {
        return repo.findByRol(rol);
    }

    // =====================================================
    // 🔍 BUSCAR
    // =====================================================
    public Usuarios buscarPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuarios buscarPorUsuario(String usuario) {
        return repo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // =====================================================
    // ✏️ ACTUALIZAR
    // =====================================================
    public Usuarios actualizar(Integer id, Usuarios datos) {

        Usuarios usuario = buscarPorId(id);

        if (!usuario.getDni().equals(datos.getDni())
                && repo.existsByDni(datos.getDni())) {
            throw new RuntimeException("DNI ya registrado");
        }

        if (!usuario.getEmail().equals(datos.getEmail())
                && repo.existsByEmail(datos.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        if (!usuario.getUsuario().equals(datos.getUsuario())
                && repo.existsByUsuario(datos.getUsuario())) {
            throw new RuntimeException("Usuario ya existe");
        }

        // 🔄 ACTUALIZAR DATOS
        usuario.setNombres(datos.getNombres());
        usuario.setApellidos(datos.getApellidos());
        usuario.setDni(datos.getDni());
        usuario.setEmail(datos.getEmail());
        usuario.setTelefono(datos.getTelefono());
        usuario.setRol(datos.getRol());
        usuario.setUsuario(datos.getUsuario());

        // 🖼️ FOTO OPCIONAL
        // Si viene foto nueva en base64, la guarda.
        // Si viene null o vacío, mantiene la anterior.
        if (datos.getFoto() != null && !datos.getFoto().isBlank()) {
            usuario.setFoto(datos.getFoto());
        }

        // 🔐 PASSWORD OPCIONAL
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            usuario.setPassword(datos.getPassword());
        }
        // 🔐 ESTADO DE CUENTA
        if (datos.getActivo() != null) {
            usuario.setActivo(datos.getActivo());
        }

        if (datos.getBloqueado() != null) {
            usuario.setBloqueado(datos.getBloqueado());
        }

        if (datos.getIntentosFallidos() != null) {
            usuario.setIntentosFallidos(datos.getIntentosFallidos());
        }

        return repo.save(usuario);
    }
    // =====================================================
    // ❌ ELIMINAR FÍSICO
    // =====================================================
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuario no existe");
        }
        repo.deleteById(id);
    }

    // =====================================================
    // 📴 ELIMINAR LÓGICO
    // =====================================================
    public void desactivar(Integer id) {
        Usuarios usuario = buscarPorId(id);
        usuario.setActivo(false);
        repo.save(usuario);
    }

    // =====================================================
    // 🔓 ACTIVAR
    // =====================================================
    public void activar(Integer id) {
        Usuarios usuario = buscarPorId(id);
        usuario.setActivo(true);
        usuario.setBloqueado(false);
        usuario.setIntentosFallidos(0);
        repo.save(usuario);
    }

    // =====================================================
    // 🔐 LOGIN POR ROL
    // =====================================================
    public Usuarios login(String usuario, String password, Rol rolEsperado) {

        Usuarios user = repo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        // 🔒 BLOQUEADO
        if (Boolean.TRUE.equals(user.getBloqueado())) {
            throw new RuntimeException("Usuario bloqueado por intentos fallidos");
        }

        // 🚫 INACTIVO
        if (!Boolean.TRUE.equals(user.getActivo())) {
            throw new RuntimeException("Usuario inactivo");
        }

        // 🚫 ROL INCORRECTO
        if (!user.getRol().equals(rolEsperado)) {
            throw new RuntimeException("No tienes acceso a este módulo");
        }

        // ❌ PASSWORD INCORRECTO
        if (!user.getPassword().equals(password)) {

            int intentos = user.getIntentosFallidos() + 1;
            user.setIntentosFallidos(intentos);

            if (intentos >= 3) {
                user.setBloqueado(true);
            }

            repo.save(user);

            throw new RuntimeException("Contraseña incorrecta");
        }

        // ✅ LOGIN OK
        user.setIntentosFallidos(0);
        user.setUltimoLogin(LocalDateTime.now());

        repo.save(user);

        return user;
    }
}