package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.entity.adulto_mayor.Salud;
import com.municipalidad.backend_siam.service.SaludService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/salud")
@RequiredArgsConstructor
public class SaludController {

    private final SaludService saludService;

    // REGISTRAR ATENCIÓN MÉDICA
    @PostMapping("/{adultoMayorId}")
    public ResponseEntity<Salud> registrarAtencion(
            @PathVariable Long adultoMayorId,
            @RequestBody Salud salud
    ) {
        return ResponseEntity.ok(
                saludService.registrarAtencion(adultoMayorId, salud)
        );
    }

    // HISTORIAL GENERAL
    @GetMapping
    public ResponseEntity<List<Salud>> listarTodas() {
        return ResponseEntity.ok(
                saludService.listarTodas()
        );
    }

    // HISTORIAL POR ADULTO MAYOR
    @GetMapping("/adulto/{adultoMayorId}")
    public ResponseEntity<List<Salud>> listarPorAdulto(
            @PathVariable Long adultoMayorId
    ) {
        return ResponseEntity.ok(
                saludService.listarPorAdultoMayor(adultoMayorId)
        );
    }

    // BUSCAR HISTORIAL POR MÉDICO
    @GetMapping("/medico")
    public ResponseEntity<List<Salud>> buscarPorMedico(
            @RequestParam String nombre
    ) {
        return ResponseEntity.ok(
                saludService.buscarPorMedico(nombre)
        );
    }

    // FILTRAR POR TIPO DE ATENCIÓN
    @GetMapping("/tipo")
    public ResponseEntity<List<Salud>> buscarPorTipo(
            @RequestParam String tipo
    ) {
        return ResponseEntity.ok(
                saludService.buscarPorTipoAtencion(tipo)
        );
    }
}