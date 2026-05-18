package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.entity.Rol;
import com.municipalidad.backend_siam.entity.Usuarios;
import com.municipalidad.backend_siam.service.UsuariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // para Angular
public class UsuariosController {

    private final UsuariosService service;

    // =====================================================
    // 🔥 CREAR
    // =====================================================
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuarios usuario) {
        return ResponseEntity.ok(service.crear(usuario));
    }

    // =====================================================
    // 📋 LISTAR
    // =====================================================
    @GetMapping
    public ResponseEntity<List<Usuarios>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuarios>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuarios>> listarPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(service.listarPorRol(rol));
    }

    // =====================================================
    // 🔍 BUSCAR POR ID
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =====================================================
    // ✏️ ACTUALIZAR
    // =====================================================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @Valid @RequestBody Usuarios usuario) {
        return ResponseEntity.ok(service.actualizar(id, usuario));
    }

    // =====================================================
    // ❌ ELIMINAR
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.ok("Usuario eliminado");
    }

    // =====================================================
    // 📴 DESACTIVAR
    // =====================================================
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivar(@PathVariable int id) {
        service.desactivar(id);
        return ResponseEntity.ok("Usuario desactivado");
    }

    // =====================================================
    // 🔐 LOGIN ADMIN
    // =====================================================
    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(
            @RequestParam String usuario,
            @RequestParam String password) {

        return ResponseEntity.ok(
                service.login(usuario, password, Rol.ADMIN)
        );
    }

    // =====================================================
    // 🔐 LOGIN CIAM
    // =====================================================
    @PostMapping("/login/ciam")
    public ResponseEntity<?> loginCiam(
            @RequestParam String usuario,
            @RequestParam String password) {

        return ResponseEntity.ok(
                service.login(usuario, password, Rol.CIAM)
        );
    }

    // =====================================================
    // 🔐 LOGIN SALUD
    // =====================================================
    @PostMapping("/login/salud")
    public ResponseEntity<?> loginSalud(
            @RequestParam String usuario,
            @RequestParam String password) {

        return ResponseEntity.ok(
                service.login(usuario, password, Rol.SALUD)
        );
    }

    // =====================================================
    // 🔐 LOGIN PENSIÓN
    // =====================================================
    @PostMapping("/login/pension")
    public ResponseEntity<?> loginPension(
            @RequestParam String usuario,
            @RequestParam String password) {

        return ResponseEntity.ok(
                service.login(usuario, password, Rol.PENSION_65)
        );
    }
}