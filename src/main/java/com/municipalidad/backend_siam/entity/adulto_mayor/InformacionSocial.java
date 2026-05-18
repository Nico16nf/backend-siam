package com.municipalidad.backend_siam.entity.adulto_mayor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionSocial {

    @Builder.Default
    @Column(name = "social_vive_solo")
    private Boolean viveSolo = false;

    @Builder.Default
    @Column(name = "social_tiene_discapacidad")
    private Boolean tieneDiscapacidad = false;

    @Builder.Default
    @Column(name = "social_situacion_abandono")
    private Boolean situacionAbandono = false;

    @Builder.Default
    @Column(name = "social_victima_violencia")
    private Boolean victimaViolencia = false;

    @Column(name = "social_tipo_vivienda", length = 80)
    private String tipoVivienda;

    @Column(name = "social_observaciones", length = 500)
    private String observacionesSociales;
}
