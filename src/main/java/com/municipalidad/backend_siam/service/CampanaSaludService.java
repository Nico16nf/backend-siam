package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.entity.adulto_mayor.CampanaSalud;
import com.municipalidad.backend_siam.repository.CampanaSaludRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampanaSaludService {

    private final CampanaSaludRepository campanaSaludRepository;

    public CampanaSalud registrar(CampanaSalud campana) {

        validarCampana(campana);

        if (campana.getEstado() == null || campana.getEstado().isBlank()) {
            campana.setEstado("PROGRAMADA");
        }

        if (campana.getTotalAtendidos() == null) {
            campana.setTotalAtendidos(0);
        }

        if (campana.getMetaAtenciones() == null) {
            campana.setMetaAtenciones(0);
        }

        return campanaSaludRepository.save(campana);
    }

    public List<CampanaSalud> listarTodas() {
        return campanaSaludRepository.findAll();
    }

    public CampanaSalud buscarPorId(Long id) {
        return campanaSaludRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña de salud no encontrada"));
    }

    public CampanaSalud actualizar(Long id, CampanaSalud campana) {

        CampanaSalud existente = buscarPorId(id);

        validarCampana(campana);

        existente.setNombreCampana(campana.getNombreCampana());
        existente.setTipoCampana(campana.getTipoCampana());
        existente.setDescripcion(campana.getDescripcion());

        existente.setLugar(campana.getLugar());
        existente.setDistrito(campana.getDistrito());
        existente.setCentroPoblado(campana.getCentroPoblado());

        existente.setFechaInicio(campana.getFechaInicio());
        existente.setFechaFin(campana.getFechaFin());

        existente.setResponsable(campana.getResponsable());
        existente.setEstablecimientoSalud(campana.getEstablecimientoSalud());

        existente.setObjetivo(campana.getObjetivo());
        existente.setServicios(campana.getServicios());
        existente.setPoblacionObjetivo(campana.getPoblacionObjetivo());

        existente.setMetaAtenciones(campana.getMetaAtenciones());
        existente.setTotalAtendidos(campana.getTotalAtendidos());

        existente.setEstado(campana.getEstado());
        existente.setObservaciones(campana.getObservaciones());

        existente.setImagen(campana.getImagen());

        return campanaSaludRepository.save(existente);
    }

    public void eliminar(Long id) {

        CampanaSalud campana = buscarPorId(id);

        if ("EN_PROCESO".equalsIgnoreCase(campana.getEstado())) {
            throw new RuntimeException("No se puede eliminar una campaña en proceso");
        }

        campanaSaludRepository.delete(campana);
    }

    public List<CampanaSalud> listarPorEstado(String estado) {

        validarEstado(estado);

        return campanaSaludRepository.findByEstado(estado.toUpperCase());
    }

    public List<CampanaSalud> listarPorTipo(String tipo) {

        if (tipo == null || tipo.isBlank()) {
            throw new RuntimeException("El tipo de campaña es obligatorio");
        }

        return campanaSaludRepository.findByTipoCampana(tipo.toUpperCase());
    }

    public List<CampanaSalud> buscarPorNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("Ingrese un nombre para buscar");
        }

        return campanaSaludRepository.findByNombreCampanaContainingIgnoreCase(nombre);
    }

    public List<CampanaSalud> listarPorFechas(LocalDate inicio, LocalDate fin) {

        if (inicio == null || fin == null) {
            throw new RuntimeException("Debe ingresar fecha de inicio y fecha fin");
        }

        if (fin.isBefore(inicio)) {
            throw new RuntimeException("La fecha fin no puede ser menor que la fecha inicio");
        }

        return campanaSaludRepository.findByFechaInicioBetween(inicio, fin);
    }

    public CampanaSalud cambiarEstado(Long id, String estado) {

        CampanaSalud campana = buscarPorId(id);

        validarEstado(estado);

        campana.setEstado(estado.toUpperCase());

        return campanaSaludRepository.save(campana);
    }

    public CampanaSalud actualizarTotalAtendidos(Long id, Integer totalAtendidos) {

        CampanaSalud campana = buscarPorId(id);

        if (totalAtendidos == null || totalAtendidos < 0) {
            throw new RuntimeException("El total de atendidos no puede ser negativo");
        }

        if (campana.getMetaAtenciones() != null &&
                campana.getMetaAtenciones() > 0 &&
                totalAtendidos > campana.getMetaAtenciones()) {
            throw new RuntimeException("El total de atendidos no puede superar la meta de atenciones");
        }

        campana.setTotalAtendidos(totalAtendidos);

        return campanaSaludRepository.save(campana);
    }

    private void validarCampana(CampanaSalud campana) {

        if (campana == null) {
            throw new RuntimeException("Los datos de la campaña son obligatorios");
        }

        if (campana.getNombreCampana() == null || campana.getNombreCampana().isBlank()) {
            throw new RuntimeException("El nombre de la campaña es obligatorio");
        }

        if (campana.getTipoCampana() == null || campana.getTipoCampana().isBlank()) {
            throw new RuntimeException("El tipo de campaña es obligatorio");
        }

        if (campana.getLugar() == null || campana.getLugar().isBlank()) {
            throw new RuntimeException("El lugar de la campaña es obligatorio");
        }

        if (campana.getFechaInicio() == null) {
            throw new RuntimeException("La fecha de inicio es obligatoria");
        }

        if (campana.getFechaFin() == null) {
            throw new RuntimeException("La fecha fin es obligatoria");
        }

        if (campana.getFechaFin().isBefore(campana.getFechaInicio())) {
            throw new RuntimeException("La fecha fin no puede ser menor que la fecha de inicio");
        }

        if (campana.getResponsable() == null || campana.getResponsable().isBlank()) {
            throw new RuntimeException("El responsable de la campaña es obligatorio");
        }

        if (campana.getEstablecimientoSalud() == null || campana.getEstablecimientoSalud().isBlank()) {
            throw new RuntimeException("El establecimiento de salud es obligatorio");
        }

        if (campana.getMetaAtenciones() != null && campana.getMetaAtenciones() < 0) {
            throw new RuntimeException("La meta de atenciones no puede ser negativa");
        }

        if (campana.getTotalAtendidos() != null && campana.getTotalAtendidos() < 0) {
            throw new RuntimeException("El total de atendidos no puede ser negativo");
        }

        if (campana.getEstado() != null && !campana.getEstado().isBlank()) {
            validarEstado(campana.getEstado());
        }
    }

    private void validarEstado(String estado) {

        if (estado == null || estado.isBlank()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        String estadoNormalizado = estado.toUpperCase();

        if (!estadoNormalizado.equals("PROGRAMADA") &&
                !estadoNormalizado.equals("EN_PROCESO") &&
                !estadoNormalizado.equals("FINALIZADA") &&
                !estadoNormalizado.equals("CANCELADA")) {

            throw new RuntimeException("Estado inválido. Use PROGRAMADA, EN_PROCESO, FINALIZADA o CANCELADA");
        }
    }
}