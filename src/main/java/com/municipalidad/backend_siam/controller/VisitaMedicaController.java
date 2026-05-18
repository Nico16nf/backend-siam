package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.entity.adulto_mayor.VisitaMedica;
import com.municipalidad.backend_siam.service.VisitaMedicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/visitas-medicas")
@RequiredArgsConstructor
public class VisitaMedicaController {

    private final VisitaMedicaService visitaMedicaService;

    @PostMapping("/{adultoMayorId}")
    public ResponseEntity<VisitaMedica> registrar(
            @PathVariable Long adultoMayorId,
            @RequestBody VisitaMedica visita
    ) {
        return ResponseEntity.ok(
                visitaMedicaService.registrar(adultoMayorId, visita)
        );
    }

    @GetMapping
    public ResponseEntity<List<VisitaMedica>> listarTodas() {
        return ResponseEntity.ok(
                visitaMedicaService.listarTodas()
        );
    }

    @GetMapping("/adulto/{adultoMayorId}")
    public ResponseEntity<List<VisitaMedica>> listarPorAdulto(
            @PathVariable Long adultoMayorId
    ) {
        return ResponseEntity.ok(
                visitaMedicaService.listarPorAdulto(adultoMayorId)
        );
    }

    @GetMapping("/seguimiento")
    public ResponseEntity<List<VisitaMedica>> listarSeguimiento() {
        return ResponseEntity.ok(
                visitaMedicaService.listarSeguimiento()
        );
    }

    @GetMapping("/referencias")
    public ResponseEntity<List<VisitaMedica>> listarReferencias() {
        return ResponseEntity.ok(
                visitaMedicaService.listarReferencias()
        );
    }
}