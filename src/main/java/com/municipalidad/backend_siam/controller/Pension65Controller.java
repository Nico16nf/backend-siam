package com.municipalidad.backend_siam.controller;

import com.municipalidad.backend_siam.dto.AdultoMayorPensionDTO;
import com.municipalidad.backend_siam.entity.adulto_mayor.AdultoMayor;
import com.municipalidad.backend_siam.entity.adulto_mayor.Pension65;
import com.municipalidad.backend_siam.service.Pension65Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pension65")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Pension65Controller {

    private final Pension65Service pension65Service;


    // Listar todas las evaluaciones
    @GetMapping
    public List<Pension65> listarEvaluaciones() {
        return pension65Service.listarEvaluaciones();
    }

    // Buscar evaluación por ID
    @GetMapping("/{id}")
    public Pension65 buscarPorId(@PathVariable Long id) {
        return pension65Service.buscarPorId(id);
    }

    // Buscar evaluación por adulto mayor
    @GetMapping("/adulto/{adultoMayorId}")
    public Pension65 buscarPorAdultoMayor(@PathVariable Long adultoMayorId) {
        return pension65Service.buscarPorAdultoMayor(adultoMayorId);
    }

    // Evaluar económicamente
    @PostMapping("/evaluar/{adultoMayorId}")
    public Pension65 evaluarEconomicamente(
            @PathVariable Long adultoMayorId,
            @RequestBody Pension65 pension65
    ) {
        return pension65Service.evaluarEconomicamente(adultoMayorId, pension65);
    }

    // Registrar pago
    @PutMapping("/pago/{adultoMayorId}")
    public Pension65 registrarPago(@PathVariable Long adultoMayorId) {
        return pension65Service.registrarPago(adultoMayorId);
    }

    // Suspender beneficiario
    @PutMapping("/suspender/{adultoMayorId}")
    public Pension65 suspenderBeneficiario(
            @PathVariable Long adultoMayorId,
            @RequestParam String motivo
    ) {
        return pension65Service.suspenderBeneficiario(adultoMayorId, motivo);
    }

    // Listar beneficiarios
    @GetMapping("/beneficiarios")
    public List<Pension65> listarBeneficiarios() {
        return pension65Service.listarBeneficiarios();
    }

    // Listar posibles beneficiarios
    @GetMapping("/posibles")
    public List<Pension65> listarPosiblesBeneficiarios() {
        return pension65Service.listarPosiblesBeneficiarios();
    }

    // Listar por estado
    @GetMapping("/estado/{estado}")
    public List<Pension65> listarPorEstado(@PathVariable String estado) {
        return pension65Service.listarPorEstado(estado);
    }

    @GetMapping("/adultos")
    public List<AdultoMayorPensionDTO> listarAdultosParaEvaluacion() {
        return pension65Service.listarAdultosParaEvaluacionDTO();
    }
}