package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.municipalidad.backend_siam.entity.adulto_mayor.enums.EstadoCivil;
import com.municipalidad.backend_siam.entity.adulto_mayor.enums.Sexo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatosPersonales {

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(nullable = false, length = 100)
    private String apellidoMaterno;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String celular;

    @Column(length = 120)
    private String correo;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto;
}