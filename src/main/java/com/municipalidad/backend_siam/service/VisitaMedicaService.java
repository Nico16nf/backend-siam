package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.entity.adulto_mayor.AdultoMayor;
import com.municipalidad.backend_siam.entity.adulto_mayor.VisitaMedica;
import com.municipalidad.backend_siam.repository.AdultoMayorRepository;
import com.municipalidad.backend_siam.repository.VisitaMedicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitaMedicaService {

    private final VisitaMedicaRepository visitaMedicaRepository;
    private final AdultoMayorRepository adultoMayorRepository;

    public VisitaMedica registrar(Long adultoMayorId, VisitaMedica visita) {

        AdultoMayor adulto = adultoMayorRepository.findById(adultoMayorId)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        if (visita == null) {
            throw new RuntimeException("Los datos de la visita médica son obligatorios");
        }

        if (visita.getMedicoResponsable() == null || visita.getMedicoResponsable().isBlank()) {
            throw new RuntimeException("El médico responsable es obligatorio");
        }

        if (visita.getEstablecimientoSalud() == null || visita.getEstablecimientoSalud().isBlank()) {
            throw new RuntimeException("El establecimiento de salud es obligatorio");
        }

        if (visita.getMotivoVisita() == null || visita.getMotivoVisita().isBlank()) {
            throw new RuntimeException("El motivo de la visita es obligatorio");
        }

        if (visita.getEvaluacionMedica() == null || visita.getEvaluacionMedica().isBlank()) {
            throw new RuntimeException("La evaluación médica es obligatoria");
        }

        if (visita.getPeso() != null && visita.getPeso() <= 0) {
            throw new RuntimeException("El peso debe ser mayor a 0");
        }

        if (visita.getTalla() != null && visita.getTalla() <= 0) {
            throw new RuntimeException("La talla debe ser mayor a 0");
        }

        if (visita.getGlucosa() != null && visita.getGlucosa() < 0) {
            throw new RuntimeException("La glucosa no puede ser negativa");
        }

        if (visita.getTemperatura() != null && visita.getTemperatura() < 30) {
            throw new RuntimeException("La temperatura ingresada no es válida");
        }

        if (visita.getFechaVisita() == null) {
            visita.setFechaVisita(LocalDate.now());
        }

        if (visita.getRequiereSeguimiento() == null) {
            visita.setRequiereSeguimiento(false);
        }

        if (visita.getRequiereReferencia() == null) {
            visita.setRequiereReferencia(false);
        }

        visita.setAdultoMayor(adulto);

        return visitaMedicaRepository.save(visita);
    }

    public List<VisitaMedica> listarTodas() {
        return visitaMedicaRepository.findAll();
    }

    public List<VisitaMedica> listarPorAdulto(Long adultoMayorId) {
        return visitaMedicaRepository.findByAdultoMayorId(adultoMayorId);
    }

    public List<VisitaMedica> listarSeguimiento() {
        return visitaMedicaRepository.findByRequiereSeguimientoTrue();
    }

    public List<VisitaMedica> listarReferencias() {
        return visitaMedicaRepository.findByRequiereReferenciaTrue();
    }
}