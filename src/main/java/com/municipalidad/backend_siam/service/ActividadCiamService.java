package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.entity.ActividadCiam;
import com.municipalidad.backend_siam.repository.ActividadCiamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActividadCiamService {

    private final ActividadCiamRepository repository;

    // Crear actividad
    public ActividadCiam guardar(ActividadCiam actividad) {
        return repository.save(actividad);
    }

    // Listar actividades
    public List<ActividadCiam> listar() {
        return repository.findAll();
    }

    // Buscar por ID
    public ActividadCiam buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Actividad no encontrada"));
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
