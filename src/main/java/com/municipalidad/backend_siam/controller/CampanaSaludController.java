package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.entity.adulto_mayor.CampanaSalud;
import com.municipalidad.backend_siam.service.CampanaSaludService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/campanas-salud")
@RequiredArgsConstructor
public class CampanaSaludController {

    private final CampanaSaludService campanaSaludService;

    @PostMapping
    public ResponseEntity<CampanaSalud> registrar(
            @RequestBody CampanaSalud campana
    ) {
        return ResponseEntity.ok(
                campanaSaludService.registrar(campana)
        );
    }

    @GetMapping
    public ResponseEntity<List<CampanaSalud>> listarTodas() {
        return ResponseEntity.ok(
                campanaSaludService.listarTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanaSalud> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                campanaSaludService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampanaSalud> actualizar(
            @PathVariable Long id,
            @RequestBody CampanaSalud campana
    ) {
        return ResponseEntity.ok(
                campanaSaludService.actualizar(id, campana)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @PathVariable Long id
    ) {
        campanaSaludService.eliminar(id);

        return ResponseEntity.ok(
                "Campaña de salud eliminada correctamente"
        );
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CampanaSalud>> listarPorEstado(
            @PathVariable String estado
    ) {
        return ResponseEntity.ok(
                campanaSaludService.listarPorEstado(estado)
        );
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CampanaSalud>> listarPorTipo(
            @PathVariable String tipo
    ) {
        return ResponseEntity.ok(
                campanaSaludService.listarPorTipo(tipo)
        );
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CampanaSalud>> buscarPorNombre(
            @RequestParam String nombre
    ) {
        return ResponseEntity.ok(
                campanaSaludService.buscarPorNombre(nombre)
        );
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<CampanaSalud>> listarPorFechas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin
    ) {
        return ResponseEntity.ok(
                campanaSaludService.listarPorFechas(inicio, fin)
        );
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<CampanaSalud> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        return ResponseEntity.ok(
                campanaSaludService.cambiarEstado(id, estado)
        );
    }

    @PutMapping("/{id}/atendidos")
    public ResponseEntity<CampanaSalud> actualizarAtendidos(
            @PathVariable Long id,
            @RequestParam Integer total
    ) {
        return ResponseEntity.ok(
                campanaSaludService.actualizarTotalAtendidos(id, total)
        );
    }
}