package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.entity.adulto_mayor.DerivacionSalud;
import com.municipalidad.backend_siam.repository.DerivacionSaludRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/derivaciones-salud")
@RequiredArgsConstructor
public class DerivacionSaludController {

    private final DerivacionSaludRepository derivacionSaludRepository;

    // =========================================================
    // LISTAR TODAS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<DerivacionSalud>> listarTodas() {

        return ResponseEntity.ok(
                derivacionSaludRepository.findAllConAdulto()
        );
    }

    // =========================================================
    // PENDIENTES
    // =========================================================

    @GetMapping("/pendientes")
    public ResponseEntity<List<DerivacionSalud>> listarPendientes() {

        return ResponseEntity.ok(
                derivacionSaludRepository.findByEstadoConAdulto("PENDIENTE")
        );
    }

    // =========================================================
    // ATENDIDAS
    // =========================================================

    @GetMapping("/atendidas")
    public ResponseEntity<List<DerivacionSalud>> listarAtendidas() {

        return ResponseEntity.ok(
                derivacionSaludRepository.findByEstadoConAdulto("ATENDIDO")
        );
    }

    // =========================================================
    // POR ADULTO MAYOR
    // =========================================================

    @GetMapping("/adulto/{adultoMayorId}")
    public ResponseEntity<List<DerivacionSalud>> listarPorAdultoMayor(
            @PathVariable Long adultoMayorId
    ) {
        return ResponseEntity.ok(
                derivacionSaludRepository.findByAdultoMayorId(adultoMayorId)
        );
    }

    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    @PutMapping("/{id}/estado")
    public ResponseEntity<DerivacionSalud> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {

        DerivacionSalud derivacion = derivacionSaludRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Derivación no encontrada")
                );

        if (estado == null || estado.isBlank()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        String estadoNormalizado = estado.toUpperCase();

        if (!estadoNormalizado.equals("PENDIENTE")
                && !estadoNormalizado.equals("ATENDIDO")
                && !estadoNormalizado.equals("CANCELADO")) {

            throw new RuntimeException(
                    "Estado inválido. Use PENDIENTE, ATENDIDO o CANCELADO"
            );
        }

        derivacion.setEstado(estadoNormalizado);

        return ResponseEntity.ok(
                derivacionSaludRepository.save(derivacion)
        );
    }

    // =========================================================
    // ASIGNAR MÉDICO
    // =========================================================

    @PutMapping("/{id}/asignar-medico")
    public ResponseEntity<DerivacionSalud> asignarMedico(
            @PathVariable Long id,
            @RequestParam String medico
    ) {

        DerivacionSalud derivacion = derivacionSaludRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Derivación no encontrada")
                );

        if (medico == null || medico.isBlank()) {
            throw new RuntimeException("El médico asignado es obligatorio");
        }

        derivacion.setMedicoAsignado(medico);

        return ResponseEntity.ok(
                derivacionSaludRepository.save(derivacion)
        );
    }
}