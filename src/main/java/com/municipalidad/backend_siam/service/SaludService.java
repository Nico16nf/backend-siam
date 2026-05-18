package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.entity.adulto_mayor.AdultoMayor;
import com.municipalidad.backend_siam.entity.adulto_mayor.Salud;
import com.municipalidad.backend_siam.repository.AdultoMayorRepository;
import com.municipalidad.backend_siam.repository.SaludRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaludService {

    private final SaludRepository saludRepository;
    private final AdultoMayorRepository adultoMayorRepository;

    // REGISTRAR ATENCIÓN MÉDICA
    public Salud registrarAtencion(Long adultoMayorId, Salud salud) {

        AdultoMayor adultoMayor = adultoMayorRepository.findById(adultoMayorId)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        validarAtencion(salud);

        if (salud.getFechaAtencion() == null) {
            salud.setFechaAtencion(LocalDate.now());
        }

        if (salud.getRequiereSeguimiento() == null) {
            salud.setRequiereSeguimiento(false);
        }

        salud.setAdultoMayor(adultoMayor);

        return saludRepository.save(salud);
    }

    // LISTAR HISTORIAL GENERAL
    public List<Salud> listarTodas() {
        return saludRepository.findAllConAdulto();
    }

    // LISTAR HISTORIAL DE UN ADULTO MAYOR
    public List<Salud> listarPorAdultoMayor(Long adultoMayorId) {

        boolean existe = adultoMayorRepository.existsById(adultoMayorId);

        if (!existe) {
            throw new RuntimeException("Adulto mayor no encontrado");
        }

        return saludRepository.findByAdultoMayorIdConAdulto(adultoMayorId);
    }

    // BUSCAR POR MÉDICO
    public List<Salud> buscarPorMedico(String medico) {

        if (medico == null || medico.isBlank()) {
            throw new RuntimeException("Ingrese el nombre del médico");
        }

        return saludRepository.buscarPorMedico(medico);
    }

    // BUSCAR POR TIPO DE ATENCIÓN
    public List<Salud> buscarPorTipoAtencion(String tipoAtencion) {

        if (tipoAtencion == null || tipoAtencion.isBlank()) {
            throw new RuntimeException("Ingrese el tipo de atención");
        }

        return saludRepository.buscarPorTipoAtencion(tipoAtencion);
    }

    // VALIDACIONES
    private void validarAtencion(Salud salud) {

        if (salud == null) {
            throw new RuntimeException("Los datos de atención médica son obligatorios");
        }

        if (salud.getMedicoResponsable() == null || salud.getMedicoResponsable().isBlank()) {
            throw new RuntimeException("El médico responsable es obligatorio");
        }

        if (salud.getEstablecimientoSalud() == null || salud.getEstablecimientoSalud().isBlank()) {
            throw new RuntimeException("El establecimiento de salud es obligatorio");
        }

        if (salud.getTipoAtencion() == null || salud.getTipoAtencion().isBlank()) {
            throw new RuntimeException("El tipo de atención es obligatorio");
        }

        if (salud.getDiagnostico() == null || salud.getDiagnostico().isBlank()) {
            throw new RuntimeException("El diagnóstico es obligatorio");
        }

        if (salud.getTratamiento() == null || salud.getTratamiento().isBlank()) {
            throw new RuntimeException("El tratamiento es obligatorio");
        }

        if (salud.getPeso() != null && salud.getPeso() <= 0) {
            throw new RuntimeException("El peso debe ser mayor a 0");
        }

        if (salud.getTalla() != null && salud.getTalla() <= 0) {
            throw new RuntimeException("La talla debe ser mayor a 0");
        }

        if (salud.getGlucosa() != null && salud.getGlucosa() < 0) {
            throw new RuntimeException("La glucosa no puede ser negativa");
        }
    }
}