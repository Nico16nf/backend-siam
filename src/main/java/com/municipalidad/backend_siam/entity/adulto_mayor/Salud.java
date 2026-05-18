package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "atenciones_salud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adulto_mayor_id", nullable = false)
    private AdultoMayor adultoMayor;

    private LocalDate fechaAtencion;

    @Column(length = 120)
    private String medicoResponsable;

    @Column(length = 120)
    private String establecimientoSalud;

    @Column(length = 150)
    private String tipoAtencion;

    @Column(length = 500)
    private String diagnostico;

    @Column(length = 500)
    private String tratamiento;

    @Column(length = 500)
    private String recomendaciones;

    @Column(length = 30)
    private String presionArterial;

    private Double peso;
    private Double talla;
    private Double glucosa;

    @Builder.Default
    private Boolean requiereSeguimiento = false;

    private LocalDate fechaProximoControl;

    @Column(length = 500)
    private String observaciones;
}