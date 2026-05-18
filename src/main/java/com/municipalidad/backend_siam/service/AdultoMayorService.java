package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.entity.adulto_mayor.*;
import com.municipalidad.backend_siam.entity.adulto_mayor.enums.ClasificacionSocioeconomica;
import com.municipalidad.backend_siam.entity.adulto_mayor.enums.NivelRiesgo;
import com.municipalidad.backend_siam.repository.AdultoMayorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AdultoMayorService {

    private final AdultoMayorRepository adultoMayorRepository;

    // =====================================================
    // 🔷 LISTAR TODOS
    // =====================================================

    public List<AdultoMayor> listarTodos() {
        return adultoMayorRepository.findAll();
    }

    // =====================================================
    // 🔷 LISTAR SOLO ACTIVOS
    // =====================================================

    public List<AdultoMayor> listarActivos() {
        return adultoMayorRepository.findByActivoTrue();
    }

    // =====================================================
    // 🔷 BUSCAR POR ID
    // =====================================================

    public AdultoMayor buscarPorId(Long id) {

        return adultoMayorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Adulto mayor no encontrado"));
    }

    // =====================================================
    // 🔷 REGISTRAR ADULTO MAYOR
    // =====================================================

    @Transactional
    public AdultoMayor registrar(AdultoMayor adultoMayor) {

        // 🔹 Validaciones generales
        validarDatosPersonales(adultoMayor);
        validarEdad(adultoMayor);
        validarCorreo(adultoMayor);
        validarDuplicadoDni(adultoMayor);

        // 🔹 Calcular SISFOH automáticamente
        if (adultoMayor.getSisfoh() != null) {
            calcularSisfoh(adultoMayor.getSisfoh());
        }

        // 🔹 Calcular nivel de riesgo
        calcularRiesgo(adultoMayor);

        // 🔹 Configurar relación Pensión 65
        configurarPension65(adultoMayor);

        // 🔹 Configurar relaciones Salud
        configurarAtencionesSalud(adultoMayor);

        // 🔹 Valores iniciales
        adultoMayor.setActivo(true);

        return adultoMayorRepository.save(adultoMayor);
    }

    // =====================================================
    // 🔷 ACTUALIZAR ADULTO MAYOR
    // =====================================================

    @Transactional
    public AdultoMayor actualizar(Long id, AdultoMayor datosActualizados) {

        AdultoMayor adulto = buscarPorId(id);

        validarDatosPersonales(datosActualizados);
        validarEdad(datosActualizados);
        validarCorreo(datosActualizados);

        // 🔹 Actualizar datos
        adulto.setDatosPersonales(datosActualizados.getDatosPersonales());
        adulto.setLugarNacimiento(datosActualizados.getLugarNacimiento());
        adulto.setUbicacionActual(datosActualizados.getUbicacionActual());
        adulto.setResponsableFamiliar(datosActualizados.getResponsableFamiliar());
        adulto.setInformacionSocial(datosActualizados.getInformacionSocial());
        adulto.setSisfoh(datosActualizados.getSisfoh());
        adulto.setEvaluacionIntegral(datosActualizados.getEvaluacionIntegral());

        // 🔹 Recalcular SISFOH
        if (adulto.getSisfoh() != null) {
            calcularSisfoh(adulto.getSisfoh());
        }

        // 🔹 Recalcular riesgo
        calcularRiesgo(adulto);

        // 🔹 Reevaluar Pensión 65
        configurarPension65(adulto);

        return adultoMayorRepository.save(adulto);
    }

    // =====================================================
    // 🔷 DESACTIVAR
    // =====================================================

    @Transactional
    public void desactivar(Long id) {

        AdultoMayor adulto = buscarPorId(id);

        adulto.setActivo(false);

        adultoMayorRepository.save(adulto);
    }

    // =====================================================
    // 🔷 AGREGAR ATENCIÓN MÉDICA
    // =====================================================

    @Transactional
    public AdultoMayor agregarAtencionSalud(Long id, Salud salud) {

        AdultoMayor adulto = buscarPorId(id);

        // 🔹 Validar atención
        validarAtencionSalud(salud);

        salud.setAdultoMayor(adulto);

        if (adulto.getAtencionesSalud() == null) {
            adulto.setAtencionesSalud(new ArrayList<>());
        }

        adulto.getAtencionesSalud().add(salud);

        return adultoMayorRepository.save(adulto);
    }

    // =====================================================
    // 🔷 ACTUALIZAR PENSIÓN 65
    // =====================================================

    @Transactional
    public AdultoMayor actualizarPension65(Long id, Pension65 pension65) {

        AdultoMayor adulto = buscarPorId(id);

        pension65.setAdultoMayor(adulto);

        adulto.setPension65(pension65);

        evaluarPension65(adulto);

        return adultoMayorRepository.save(adulto);
    }

    // =====================================================
    // 🔷 VALIDAR DATOS PERSONALES
    // =====================================================

    private void validarDatosPersonales(AdultoMayor adultoMayor) {

        if (adultoMayor.getDatosPersonales() == null) {
            throw new RuntimeException("Datos personales obligatorios");
        }

        DatosPersonales datos = adultoMayor.getDatosPersonales();

        if (datos.getDni() == null ||
                !datos.getDni().matches("\\d{8}")) {

            throw new RuntimeException("El DNI debe tener 8 dígitos");
        }

        if (datos.getNombres() == null ||
                datos.getNombres().isBlank()) {

            throw new RuntimeException("Nombres obligatorios");
        }

        if (datos.getApellidoPaterno() == null ||
                datos.getApellidoPaterno().isBlank()) {

            throw new RuntimeException("Apellido paterno obligatorio");
        }

        if (datos.getApellidoMaterno() == null ||
                datos.getApellidoMaterno().isBlank()) {

            throw new RuntimeException("Apellido materno obligatorio");
        }
    }

    // =====================================================
    // 🔷 VALIDAR EDAD
    // =====================================================

    private void validarEdad(AdultoMayor adultoMayor) {

        LocalDate fechaNacimiento =
                adultoMayor.getDatosPersonales().getFechaNacimiento();

        if (fechaNacimiento == null) {
            throw new RuntimeException("Fecha de nacimiento obligatoria");
        }

        int edad = Period.between(
                fechaNacimiento,
                LocalDate.now()
        ).getYears();

        if (edad < 65) {
            throw new RuntimeException(
                    "La persona no califica como adulto mayor"
            );
        }
    }

    // =====================================================
    // 🔷 VALIDAR CORREO
    // =====================================================

    private void validarCorreo(AdultoMayor adultoMayor) {

        String correo =
                adultoMayor.getDatosPersonales().getCorreo();

        if (correo != null && !correo.isBlank()) {

            String regex =
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

            boolean valido =
                    Pattern.matches(regex, correo);

            if (!valido) {
                throw new RuntimeException(
                        "Correo electrónico inválido"
                );
            }
        }
    }

    // =====================================================
    // 🔷 VALIDAR DNI DUPLICADO
    // =====================================================

    private void validarDuplicadoDni(AdultoMayor adultoMayor) {

        String dni =
                adultoMayor.getDatosPersonales().getDni();

        adultoMayorRepository.findByDatosPersonalesDni(dni)
                .ifPresent(a -> {
                    throw new RuntimeException(
                            "Ya existe un adulto mayor con ese DNI"
                    );
                });
    }

    // =====================================================
    // 🔷 CALCULAR SISFOH
    // =====================================================

    private void calcularSisfoh(Sisfoh sisfoh) {

        int puntaje = 0;

        // 🔹 Factores positivos
        if (Boolean.TRUE.equals(sisfoh.getTieneIngresos()))
            puntaje += 20;

        if (sisfoh.getIngresoMensual() != null &&
                sisfoh.getIngresoMensual() > 500)
            puntaje += 25;

        if (Boolean.TRUE.equals(sisfoh.getViviendaPropia()))
            puntaje += 15;

        if (Boolean.TRUE.equals(sisfoh.getTieneAgua()))
            puntaje += 10;

        if (Boolean.TRUE.equals(sisfoh.getTieneLuz()))
            puntaje += 10;

        if (Boolean.TRUE.equals(sisfoh.getTieneDesague()))
            puntaje += 10;

        if (Boolean.TRUE.equals(sisfoh.getRecibeApoyoFamiliar()))
            puntaje += 10;

        // 🔹 Factores negativos
        if (Boolean.TRUE.equals(sisfoh.getViveSolo()))
            puntaje -= 15;

        if (Boolean.TRUE.equals(sisfoh.getDiscapacidad()))
            puntaje -= 10;

        if (Boolean.TRUE.equals(sisfoh.getEnfermedadCronica()))
            puntaje -= 10;

        if (Boolean.TRUE.equals(sisfoh.getSituacionAbandono()))
            puntaje -= 20;

        // 🔹 Evitar negativos
        if (puntaje < 0)
            puntaje = 0;

        sisfoh.setPuntajeSisfoh(puntaje);

        sisfoh.setFechaEvaluacion(LocalDate.now());

        sisfoh.setSisfohVigente(true);

        if (sisfoh.getFechaVencimiento() == null) {

            sisfoh.setFechaVencimiento(
                    LocalDate.now().plusYears(3)
            );
        }

        // 🔹 Clasificación automática
        if (puntaje <= 25) {

            sisfoh.setClasificacionSocioeconomica(
                    ClasificacionSocioeconomica.POBRE_EXTREMO
            );

        } else if (puntaje <= 50) {

            sisfoh.setClasificacionSocioeconomica(
                    ClasificacionSocioeconomica.POBRE
            );

        } else {

            sisfoh.setClasificacionSocioeconomica(
                    ClasificacionSocioeconomica.NO_POBRE
            );
        }
    }

    // =====================================================
    // 🔷 CALCULAR NIVEL DE RIESGO
    // =====================================================

    private void calcularRiesgo(AdultoMayor adulto) {

        int riesgo = 0;

        InformacionSocial social =
                adulto.getInformacionSocial();

        Sisfoh sisfoh =
                adulto.getSisfoh();

        // 🔹 Factores sociales
        if (social != null) {

            if (Boolean.TRUE.equals(social.getViveSolo()))
                riesgo += 20;

            if (Boolean.TRUE.equals(
                    social.getSituacionAbandono()))
                riesgo += 30;

            if (Boolean.TRUE.equals(
                    social.getVictimaViolencia()))
                riesgo += 30;

            if (Boolean.TRUE.equals(
                    social.getTieneDiscapacidad()))
                riesgo += 15;
        }

        // 🔹 Factores económicos
        if (sisfoh != null) {

            if (sisfoh.getClasificacionSocioeconomica()
                    == ClasificacionSocioeconomica.POBRE)
                riesgo += 15;

            if (sisfoh.getClasificacionSocioeconomica()
                    == ClasificacionSocioeconomica.POBRE_EXTREMO)
                riesgo += 30;

            if (Boolean.TRUE.equals(
                    sisfoh.getEnfermedadCronica()))
                riesgo += 15;
        }

        if (adulto.getEvaluacionIntegral() == null) {
            adulto.setEvaluacionIntegral(
                    new EvaluacionIntegral()
            );
        }

        // 🔹 Clasificación final
        if (riesgo >= 70) {

            adulto.getEvaluacionIntegral()
                    .setNivelRiesgo(NivelRiesgo.CRITICO);

        } else if (riesgo >= 45) {

            adulto.getEvaluacionIntegral()
                    .setNivelRiesgo(NivelRiesgo.ALTO);

        } else if (riesgo >= 20) {

            adulto.getEvaluacionIntegral()
                    .setNivelRiesgo(NivelRiesgo.MEDIO);

        } else {

            adulto.getEvaluacionIntegral()
                    .setNivelRiesgo(NivelRiesgo.BAJO);
        }
    }

    // =====================================================
    // 🔷 CONFIGURAR PENSIÓN 65
    // =====================================================

    private void configurarPension65(AdultoMayor adulto) {

        if (adulto.getPension65() == null) {
            return;
        }

        adulto.getPension65().setAdultoMayor(adulto);

        evaluarPension65(adulto);
    }

    // =====================================================
    // 🔷 EVALUAR PENSIÓN 65
    // =====================================================

    private void evaluarPension65(AdultoMayor adulto) {

        if (adulto.getPension65() == null ||
                adulto.getSisfoh() == null) {
            return;
        }

        Pension65 pension65 =
                adulto.getPension65();

        boolean califica =

                adulto.getSisfoh()
                        .getClasificacionSocioeconomica()
                        == ClasificacionSocioeconomica.POBRE

                        ||

                        adulto.getSisfoh()
                                .getClasificacionSocioeconomica()
                                == ClasificacionSocioeconomica.POBRE_EXTREMO;

        pension65.setPosibleBeneficiario(califica);

        if (pension65.getEstado() == null) {

            pension65.setEstado(
                    califica
                            ? "PENDIENTE"
                            : "NO_CALIFICA"
            );
        }
    }

    // =====================================================
    // 🔷 CONFIGURAR ATENCIONES SALUD
    // =====================================================

    private void configurarAtencionesSalud(
            AdultoMayor adultoMayor
    ) {

        if (adultoMayor.getAtencionesSalud() == null)
            return;

        adultoMayor.getAtencionesSalud()
                .forEach(s ->
                        s.setAdultoMayor(adultoMayor));
    }

    // =====================================================
    // 🔷 VALIDAR ATENCIÓN MÉDICA
    // =====================================================

    private void validarAtencionSalud(Salud salud) {

        if (salud.getFechaAtencion() == null) {

            salud.setFechaAtencion(LocalDate.now());
        }

        if (salud.getTipoAtencion() == null ||
                salud.getTipoAtencion().isBlank()) {

            throw new RuntimeException(
                    "Tipo de atención obligatorio"
            );
        }

        if (salud.getDiagnostico() == null ||
                salud.getDiagnostico().isBlank()) {

            throw new RuntimeException(
                    "Diagnóstico obligatorio"
            );
        }
    }
    public AdultoMayor buscarPorDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new RuntimeException("El DNI debe tener 8 dígitos");
        }

        return adultoMayorRepository.findByDatosPersonalesDni(dni)
                .orElseThrow(() -> new RuntimeException("No se encontró adulto mayor con ese DNI"));
    }

    @Transactional
    public AdultoMayor agregarVisitaDomiciliaria(Long id, VisitaDomiciliaria visita) {

        AdultoMayor adulto = buscarPorId(id);

        visita.setAdultoMayor(adulto);

        if (adulto.getVisitasDomiciliarias() == null) {
            adulto.setVisitasDomiciliarias(new ArrayList<>());
        }

        if (visita.getFechaVisita() == null) {
            visita.setFechaVisita(LocalDate.now());
        }

        // Si está en abandono o violencia, marcar prioritario automáticamente
        if (adulto.getInformacionSocial() != null) {
            boolean abandono = Boolean.TRUE.equals(adulto.getInformacionSocial().getSituacionAbandono());
            boolean violencia = Boolean.TRUE.equals(adulto.getInformacionSocial().getVictimaViolencia());
            boolean viveSolo = Boolean.TRUE.equals(adulto.getInformacionSocial().getViveSolo());

            if (abandono || violencia || viveSolo) {
                visita.setCasoPrioritario(true);
                visita.setRequiereSeguimiento(true);
            }
        }

        adulto.getVisitasDomiciliarias().add(visita);

        return adultoMayorRepository.save(adulto);
    }

    @Transactional
    public AdultoMayor derivarASalud(Long id, DerivacionSalud derivacion) {

        AdultoMayor adulto = buscarPorId(id);

        if (derivacion == null) {
            throw new RuntimeException("La derivación no puede estar vacía");
        }

        if (derivacion.getResponsableCiam() == null ||
                derivacion.getResponsableCiam().isBlank()) {
            throw new RuntimeException("El responsable CIAM es obligatorio");
        }

        if (derivacion.getMotivoDerivacion() == null ||
                derivacion.getMotivoDerivacion().isBlank()) {
            throw new RuntimeException("El motivo de derivación es obligatorio");
        }

        if (derivacion.getObservaciones() == null ||
                derivacion.getObservaciones().isBlank()) {
            throw new RuntimeException("Las observaciones son obligatorias");
        }

        if (adulto.getDerivacionesSalud() == null) {
            adulto.setDerivacionesSalud(new ArrayList<>());
        }

        boolean yaTienePendiente = adulto.getDerivacionesSalud()
                .stream()
                .anyMatch(d -> "PENDIENTE".equalsIgnoreCase(d.getEstado()));

        if (yaTienePendiente) {
            throw new RuntimeException("Este adulto mayor ya tiene una derivación pendiente a Salud");
        }

        derivacion.setAdultoMayor(adulto);

        if (derivacion.getFechaDerivacion() == null) {
            derivacion.setFechaDerivacion(LocalDate.now());
        }

        derivacion.setEstado("PENDIENTE");

        boolean prioridad = evaluarPrioridadDerivacion(adulto);

        if (prioridad) {
            derivacion.setPrioridadAlta(true);
        }

        adulto.getDerivacionesSalud().add(derivacion);

        return adultoMayorRepository.save(adulto);
    }

    private boolean evaluarPrioridadDerivacion(AdultoMayor adulto) {

        boolean riesgoAltoOCritico =
                adulto.getEvaluacionIntegral() != null &&
                        adulto.getEvaluacionIntegral().getNivelRiesgo() != null &&
                        (
                                adulto.getEvaluacionIntegral().getNivelRiesgo() == NivelRiesgo.ALTO ||
                                        adulto.getEvaluacionIntegral().getNivelRiesgo() == NivelRiesgo.CRITICO
                        );

        boolean abandono =
                adulto.getInformacionSocial() != null &&
                        Boolean.TRUE.equals(adulto.getInformacionSocial().getSituacionAbandono());

        boolean violencia =
                adulto.getInformacionSocial() != null &&
                        Boolean.TRUE.equals(adulto.getInformacionSocial().getVictimaViolencia());

        boolean viveSolo =
                adulto.getInformacionSocial() != null &&
                        Boolean.TRUE.equals(adulto.getInformacionSocial().getViveSolo());

        boolean discapacidad =
                adulto.getInformacionSocial() != null &&
                        Boolean.TRUE.equals(adulto.getInformacionSocial().getTieneDiscapacidad());

        return riesgoAltoOCritico || abandono || violencia || viveSolo || discapacidad;
    }
}