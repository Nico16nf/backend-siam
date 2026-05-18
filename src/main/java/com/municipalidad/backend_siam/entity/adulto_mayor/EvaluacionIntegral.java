package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.municipalidad.backend_siam.entity.adulto_mayor.enums.NivelRiesgo;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionIntegral {

    @Column(length = 120)
    private String estadoFisico;

    @Column(length = 120)
    private String estadoMental;

    @Column(length = 120)
    private String estadoEmocional;

    @Column(length = 120)
    private String estadoSocial;

    @Enumerated(EnumType.STRING)
    private NivelRiesgo nivelRiesgo;
}