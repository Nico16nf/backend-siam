package com.municipalidad.backend_siam.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdultoMayorPensionDTO {

    private Long id;

    private String dni;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;

    private Integer edad;

    private String distrito;

    private String clasificacionSisfoh;
    private Boolean sisfohVigente;

    private String estadoPension;
    private Boolean beneficiario;
    private Boolean posibleBeneficiario;
}
