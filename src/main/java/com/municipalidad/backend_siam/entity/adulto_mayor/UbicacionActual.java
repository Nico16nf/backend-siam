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
public class UbicacionActual {

    @Column(length = 100)
    private String departamento;

    @Column(length = 100)
    private String provincia;

    @Column(length = 100)
    private String distrito;

    @Column(length = 120)
    private String centroPoblado;

    @Column(length = 120)
    private String comunidad;

    @Column(length = 250)
    private String direccion;
}