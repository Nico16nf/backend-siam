package com.municipalidad.backend_siam.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Pension65Response {

    private Long idPension65;
    private Long idAdultoMayor;

    private String dni;
    private String nombres;
    private String apellidos;
    private Integer edad;

    private Boolean cumpleEdad;
    private Boolean cumpleDni;
    private Boolean cumpleSisfoh;
    private Boolean noRecibeOtraPension;
    private Boolean tieneFormulario1000;

    private Boolean beneficiario;
    private Boolean posibleBeneficiario;

    private String estado;
    private String motivoEvaluacion;

    private LocalDate fechaEvaluacion;
    private LocalDate fechaAfiliacion;
    private LocalDate fechaUltimoPago;
    private LocalDate fechaProximoPago;

    private Double montoProximoPago;
}