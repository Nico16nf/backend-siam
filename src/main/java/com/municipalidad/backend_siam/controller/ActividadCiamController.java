package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.entity.ActividadCiam;
import com.municipalidad.backend_siam.service.ActividadCiamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades-ciam")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActividadCiamController {

    private final ActividadCiamService service;

    // Crear actividad
    @PostMapping
    public ActividadCiam crear(@RequestBody ActividadCiam actividad) {
        return service.guardar(actividad);
    }

    // Listar actividades
    @GetMapping
    public List<ActividadCiam> listar() {
        return service.listar();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ActividadCiam buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // Eliminar actividad
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
