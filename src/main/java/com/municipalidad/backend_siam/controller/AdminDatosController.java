package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.service.AdminDatosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/datos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminDatosController {

    private final AdminDatosService service;

    // Backup manual
    @PostMapping("/backup")
    public ResponseEntity<?> backupManual() {
        return ResponseEntity.ok(
                Map.of("mensaje", "Backup generado correctamente",
                        "archivo", service.generarBackupManual())
        );
    }

    // Restaurar BD
    @PostMapping("/restaurar")
    public ResponseEntity<?> restaurar(@RequestParam String rutaArchivo) {
        return ResponseEntity.ok(
                Map.of("mensaje", service.restaurarBackup(rutaArchivo))
        );
    }

    // Backups automáticos
    @PostMapping("/backup-automatico")
    public ResponseEntity<?> configurarBackupAutomatico(@RequestParam String frecuencia) {
        return ResponseEntity.ok(
                Map.of("mensaje", service.configurarBackupsAutomaticos(frecuencia))
        );
    }

    // Exportar base completa
    @GetMapping("/exportar")
    public ResponseEntity<?> exportarBaseCompleta() {
        return ResponseEntity.ok(
                Map.of("archivo", service.exportarBaseCompleta())
        );
    }

    // Migración de datos
    @PostMapping("/migrar")
    public ResponseEntity<?> migrarDatos() {
        return ResponseEntity.ok(
                Map.of("mensaje", service.migrarDatos())
        );
    }

    // Limpieza de duplicados
    @DeleteMapping("/duplicados")
    public ResponseEntity<?> limpiarDuplicados() {
        return ResponseEntity.ok(
                Map.of("mensaje", service.limpiarDuplicados())
        );
    }
}
