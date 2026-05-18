package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "visitas_domiciliarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitaDomiciliaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con adulto mayor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adulto_mayor_id", nullable = false)
    @JsonIgnore
    private AdultoMayor adultoMayor;

    private LocalDate fechaVisita;

    @Column(length = 120)
    private String responsableVisita;

    @Column(length = 120)
    private String cargoResponsable;
    // Ejemplo: Responsable CIAM, Trabajador social, Promotor social

    @Column(length = 150)
    private String motivoVisita;
    // Abandono, violencia familiar, vive solo, seguimiento social, verificación vivienda

    @Column(length = 120)
    private String estadoVivienda;
    // Buena, regular, precaria, inhabitable

    @Column(length = 120)
    private String condicionAdultoMayor;
    // Estable, vulnerable, crítico, requiere atención urgente

    @Column(length = 500)
    private String observaciones;

    @Column(length = 500)
    private String recomendaciones;

    @Builder.Default
    private Boolean requiereSeguimiento = false;

    private LocalDate fechaProximaVisita;

    @Builder.Default
    private Boolean casoPrioritario = false;

    // Evidencia en base64
    @Lob
    private String fotoEvidencia;

    @PrePersist
    public void prePersist() {
        if (this.fechaVisita == null) {
            this.fechaVisita = LocalDate.now();
        }
    }
}