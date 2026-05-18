package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.dto.AdultoMayorPensionDTO;
import com.municipalidad.backend_siam.entity.adulto_mayor.AdultoMayor;
import com.municipalidad.backend_siam.entity.adulto_mayor.Pension65;
import com.municipalidad.backend_siam.entity.adulto_mayor.enums.ClasificacionSocioeconomica;
import com.municipalidad.backend_siam.repository.AdultoMayorRepository;
import com.municipalidad.backend_siam.repository.Pension65Repository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Pension65Service {

    private final Pension65Repository pension65Repository;
    private final AdultoMayorRepository adultoMayorRepository;

    public List<AdultoMayor> listarAdultosParaEvaluacion() {
        return adultoMayorRepository.findAll();
    }

    public List<Pension65> listarEvaluaciones() {
        return pension65Repository.findAll();
    }

    public Pension65 buscarPorId(Long id) {
        return pension65Repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación Pensión 65 no encontrada"));
    }

    public Pension65 buscarPorAdultoMayor(Long adultoMayorId) {
        return pension65Repository.findByAdultoMayorId(adultoMayorId)
                .orElseThrow(() -> new RuntimeException("Este adulto mayor aún no tiene evaluación Pensión 65"));
    }

    @Transactional
    public Pension65 evaluarEconomicamente(Long adultoMayorId, Pension65 datosEvaluacion) {

        AdultoMayor adulto = adultoMayorRepository.findById(adultoMayorId)
                .orElseThrow(() -> new RuntimeException("Adulto mayor no encontrado"));

        // Si está inactivo o null, se activa automáticamente para permitir evaluación.
        if (adulto.getActivo() == null || Boolean.FALSE.equals(adulto.getActivo())) {
            adulto.setActivo(true);
            adultoMayorRepository.save(adulto);
        }

        Pension65 pension65 = pension65Repository.findByAdultoMayorId(adultoMayorId)
                .orElse(new Pension65());

        pension65.setAdultoMayor(adulto);

        if (datosEvaluacion != null && datosEvaluacion.getObservaciones() != null) {
            pension65.setObservaciones(datosEvaluacion.getObservaciones());
        }

        boolean cumpleEdad = calcularEdad(adulto) >= 65;
        boolean cumpleDni = validarDni(adulto);
        boolean cumpleSisfoh = validarSisfoh(adulto);
        boolean tieneSisfohVigente = validarSisfohVigente(adulto);

        boolean califica = cumpleEdad && cumpleDni && cumpleSisfoh && tieneSisfohVigente;

        pension65.setCumpleEdad(cumpleEdad);
        pension65.setCumpleDni(cumpleDni);
        pension65.setCumpleSisfoh(cumpleSisfoh);
        pension65.setFechaEvaluacion(LocalDate.now());

        if (califica) {
            pension65.setBeneficiario(true);
            pension65.setPosibleBeneficiario(false);
            pension65.setEstado("BENEFICIARIO");
            pension65.setFechaAfiliacion(LocalDate.now());
            pension65.setMontoUltimoPago(350.00);
            pension65.setFechaUltimoPago(null);
            pension65.setObservaciones(generarObservacionExitosa());
            pension65.setMotivoEvaluacion("Cumple con los criterios principales de Pensión 65.");
        } else {
            pension65.setBeneficiario(false);
            pension65.setPosibleBeneficiario(evaluarPosibleBeneficiario(adulto));
            pension65.setEstado(pension65.getPosibleBeneficiario() ? "PENDIENTE" : "NO_CALIFICA");
            pension65.setMontoUltimoPago(0.00);
            pension65.setFechaUltimoPago(null);
            pension65.setFechaAfiliacion(null);
            pension65.setObservaciones(
                    generarMotivoNoCalifica(cumpleEdad, cumpleDni, cumpleSisfoh, tieneSisfohVigente)
            );
            pension65.setMotivoEvaluacion(
                    generarMotivoNoCalifica(cumpleEdad, cumpleDni, cumpleSisfoh, tieneSisfohVigente)
            );
        }

        adulto.setPension65(pension65);

        return pension65Repository.save(pension65);
    }

    @Transactional
    public Pension65 registrarPago(Long adultoMayorId) {

        Pension65 pension65 = pension65Repository.findByAdultoMayorId(adultoMayorId)
                .orElseThrow(() -> new RuntimeException("Evaluación Pensión 65 no encontrada"));

        if (!Boolean.TRUE.equals(pension65.getBeneficiario())) {
            throw new RuntimeException("No se puede registrar pago porque no es beneficiario");
        }

        pension65.setFechaUltimoPago(LocalDate.now());
        pension65.setMontoUltimoPago(350.00);
        pension65.setFechaProximoPago(LocalDate.now().plusMonths(2));
        pension65.setMontoProximoPago(350.00);
        pension65.setEstado("ACTIVO");

        return pension65Repository.save(pension65);
    }

    @Transactional
    public Pension65 suspenderBeneficiario(Long adultoMayorId, String motivo) {

        Pension65 pension65 = pension65Repository.findByAdultoMayorId(adultoMayorId)
                .orElseThrow(() -> new RuntimeException("Evaluación Pensión 65 no encontrada"));

        pension65.setBeneficiario(false);
        pension65.setPosibleBeneficiario(false);
        pension65.setEstado("SUSPENDIDO");
        pension65.setObservaciones(motivo);

        return pension65Repository.save(pension65);
    }

    public List<Pension65> listarBeneficiarios() {
        return pension65Repository.findByBeneficiarioTrue();
    }

    public List<Pension65> listarPosiblesBeneficiarios() {
        return pension65Repository.findByPosibleBeneficiarioTrue();
    }

    public List<Pension65> listarPorEstado(String estado) {
        return pension65Repository.findByEstadoIgnoreCase(estado);
    }

    public List<AdultoMayorPensionDTO> listarAdultosParaEvaluacionDTO() {

        return adultoMayorRepository.findAll()
                .stream()
                .map(adulto -> {
                    Pension65 pension = adulto.getPension65();

                    return AdultoMayorPensionDTO.builder()
                            .id(adulto.getId())
                            .dni(adulto.getDatosPersonales() != null ? adulto.getDatosPersonales().getDni() : "")
                            .nombres(adulto.getDatosPersonales() != null ? adulto.getDatosPersonales().getNombres() : "")
                            .apellidoPaterno(adulto.getDatosPersonales() != null ? adulto.getDatosPersonales().getApellidoPaterno() : "")
                            .apellidoMaterno(adulto.getDatosPersonales() != null ? adulto.getDatosPersonales().getApellidoMaterno() : "")
                            .edad(calcularEdad(adulto))
                            .distrito(adulto.getUbicacionActual() != null ? adulto.getUbicacionActual().getDistrito() : "Sin ubicación")
                            .clasificacionSisfoh(
                                    adulto.getSisfoh() != null &&
                                            adulto.getSisfoh().getClasificacionSocioeconomica() != null
                                            ? adulto.getSisfoh().getClasificacionSocioeconomica().name()
                                            : "SIN_SISFOH"
                            )
                            .sisfohVigente(
                                    adulto.getSisfoh() != null &&
                                            Boolean.TRUE.equals(adulto.getSisfoh().getSisfohVigente())
                            )
                            .estadoPension(
                                    pension != null && pension.getEstado() != null
                                            ? pension.getEstado()
                                            : "SIN_EVALUAR"
                            )
                            .beneficiario(
                                    pension != null &&
                                            Boolean.TRUE.equals(pension.getBeneficiario())
                            )
                            .posibleBeneficiario(
                                    pension != null &&
                                            Boolean.TRUE.equals(pension.getPosibleBeneficiario())
                            )
                            .build();
                })
                .toList();
    }

    private int calcularEdad(AdultoMayor adulto) {
        if (adulto.getDatosPersonales() == null ||
                adulto.getDatosPersonales().getFechaNacimiento() == null) {
            return 0;
        }

        return Period.between(
                adulto.getDatosPersonales().getFechaNacimiento(),
                LocalDate.now()
        ).getYears();
    }

    private boolean validarDni(AdultoMayor adulto) {
        return adulto.getDatosPersonales() != null
                && adulto.getDatosPersonales().getDni() != null
                && adulto.getDatosPersonales().getDni().matches("\\d{8}");
    }

    private boolean validarSisfoh(AdultoMayor adulto) {
        if (adulto.getSisfoh() == null ||
                adulto.getSisfoh().getClasificacionSocioeconomica() == null) {
            return false;
        }

        return adulto.getSisfoh().getClasificacionSocioeconomica()
                == ClasificacionSocioeconomica.POBRE_EXTREMO;
    }

    private boolean validarSisfohVigente(AdultoMayor adulto) {
        if (adulto.getSisfoh() == null) {
            return false;
        }

        if (adulto.getSisfoh().getSisfohVigente() == null) {
            return false;
        }

        if (!adulto.getSisfoh().getSisfohVigente()) {
            return false;
        }

        if (adulto.getSisfoh().getFechaVencimiento() == null) {
            return true;
        }

        return !adulto.getSisfoh().getFechaVencimiento().isBefore(LocalDate.now());
    }

    private boolean evaluarPosibleBeneficiario(AdultoMayor adulto) {
        if (adulto.getSisfoh() == null ||
                adulto.getSisfoh().getClasificacionSocioeconomica() == null) {
            return false;
        }

        return adulto.getSisfoh().getClasificacionSocioeconomica() == ClasificacionSocioeconomica.POBRE
                || adulto.getSisfoh().getClasificacionSocioeconomica() == ClasificacionSocioeconomica.POBRE_EXTREMO;
    }

    private String generarObservacionExitosa() {
        return "Cumple criterios principales: edad mínima, DNI válido, SISFOH vigente y clasificación pobre extremo.";
    }

    private String generarMotivoNoCalifica(
            boolean cumpleEdad,
            boolean cumpleDni,
            boolean cumpleSisfoh,
            boolean tieneSisfohVigente
    ) {
        StringBuilder motivo = new StringBuilder("No califica por: ");

        if (!cumpleEdad) {
            motivo.append("no cumple edad mínima de 65 años; ");
        }

        if (!cumpleDni) {
            motivo.append("DNI inválido o faltante; ");
        }

        if (!cumpleSisfoh) {
            motivo.append("no tiene clasificación SISFOH pobre extremo; ");
        }

        if (!tieneSisfohVigente) {
            motivo.append("SISFOH vencido o no vigente; ");
        }

        return motivo.toString();
    }
}