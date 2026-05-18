package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "visitas_medicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Adulto mayor visitado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adulto_mayor_id", nullable = false)
    @JsonIgnore
    private AdultoMayor adultoMayor;

    private LocalDate fechaVisita;

    @Column(length = 120)
    private String medicoResponsable;

    @Column(length = 120)
    private String establecimientoSalud;

    @Column(length = 150)
    private String motivoVisita;

    @Column(length = 500)
    private String evaluacionMedica;

    @Column(length = 500)
    private String diagnosticoPresuntivo;

    @Column(length = 500)
    private String tratamientoIndicado;

    @Column(length = 500)
    private String recomendaciones;

    // Signos vitales
    @Column(length = 30)
    private String presionArterial;

    private Double peso;

    private Double talla;

    private Double glucosa;

    private Double temperatura;

    private Integer frecuenciaCardiaca;

    private Integer saturacionOxigeno;

    @Builder.Default
    private Boolean requiereReferencia = false;

    @Column(length = 150)
    private String referenciaA;

    @Builder.Default
    private Boolean requiereSeguimiento = false;

    private LocalDate fechaProximaVisita;

    @Column(length = 500)
    private String observaciones;

    @PrePersist
    public void prePersist() {
        if (fechaVisita == null) {
            fechaVisita = LocalDate.now();
        }

        if (requiereReferencia == null) {
            requiereReferencia = false;
        }

        if (requiereSeguimiento == null) {
            requiereSeguimiento = false;
        }
    }
}