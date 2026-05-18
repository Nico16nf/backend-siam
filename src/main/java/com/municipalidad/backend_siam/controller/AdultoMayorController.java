package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.entity.adulto_mayor.*;
import com.municipalidad.backend_siam.service.AdultoMayorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/adultos-mayores")
@RequiredArgsConstructor
public class AdultoMayorController {

    private final AdultoMayorService adultoMayorService;

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<AdultoMayor>> listarTodos() {
        return ResponseEntity.ok(adultoMayorService.listarTodos());
    }

    // LISTAR ACTIVOS
    @GetMapping("/activos")
    public ResponseEntity<List<AdultoMayor>> listarActivos() {
        return ResponseEntity.ok(adultoMayorService.listarActivos());
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<AdultoMayor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(adultoMayorService.buscarPorId(id));
    }

    // REGISTRAR ADULTO MAYOR
    @PostMapping
    public ResponseEntity<AdultoMayor> registrar(
            @RequestBody AdultoMayor adultoMayor
    ) {
        return ResponseEntity.ok(adultoMayorService.registrar(adultoMayor));
    }

    // ACTUALIZAR ADULTO MAYOR
    @PutMapping("/{id}")
    public ResponseEntity<AdultoMayor> actualizar(
            @PathVariable Long id,
            @RequestBody AdultoMayor adultoMayor
    ) {
        return ResponseEntity.ok(adultoMayorService.actualizar(id, adultoMayor));
    }

    // DESACTIVAR ADULTO MAYOR
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivar(@PathVariable Long id) {
        adultoMayorService.desactivar(id);
        return ResponseEntity.ok("Adulto mayor desactivado correctamente");
    }

    // AGREGAR ATENCIÓN DE SALUD
    @PostMapping("/{id}/salud")
    public ResponseEntity<AdultoMayor> agregarAtencionSalud(
            @PathVariable Long id,
            @RequestBody Salud salud
    ) {
        return ResponseEntity.ok(
                adultoMayorService.agregarAtencionSalud(id, salud)
        );
    }

    // ACTUALIZAR PENSIÓN 65
    @PutMapping("/{id}/pension65")
    public ResponseEntity<AdultoMayor> actualizarPension65(
            @PathVariable Long id,
            @RequestBody Pension65 pension65
    ) {
        return ResponseEntity.ok(
                adultoMayorService.actualizarPension65(id, pension65)
        );
    }

    // BUSCAR POR DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<AdultoMayor> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(adultoMayorService.buscarPorDni(dni));
    }

    // AGREGAR VISITA DOMICILIARIA
    @PostMapping("/{id}/visitas")
    public ResponseEntity<AdultoMayor> agregarVisitaDomiciliaria(
            @PathVariable Long id,
            @RequestBody VisitaDomiciliaria visita
    ) {
        return ResponseEntity.ok(
                adultoMayorService.agregarVisitaDomiciliaria(id, visita)
        );
    }

    // DERIVAR A SALUD DESDE CIAM
    @PostMapping("/{id}/derivar-salud")
    public ResponseEntity<AdultoMayor> derivarASalud(
            @PathVariable Long id,
            @RequestBody DerivacionSalud derivacion
    ) {
        return ResponseEntity.ok(
                adultoMayorService.derivarASalud(id, derivacion)
        );
    }
}
