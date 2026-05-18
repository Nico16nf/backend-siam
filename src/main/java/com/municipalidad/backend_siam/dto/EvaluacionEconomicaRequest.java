package com.municipalidad.backend_siam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluacionEconomicaRequest {

    private Boolean noRecibeOtraPension;
    private Boolean tieneFormulario1000;
    private Boolean tieneDiscapacidad;
    private String observaciones;
}
