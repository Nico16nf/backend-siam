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
public class LugarNacimiento {

    @Column(length = 100)
    private String departamentoNacimiento;

    @Column(length = 100)
    private String provinciaNacimiento;

    @Column(length = 100)
    private String distritoNacimiento;

    @Column(length = 150)
    private String comunidadNacimiento;
}
