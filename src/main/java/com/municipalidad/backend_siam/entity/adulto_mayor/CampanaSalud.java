package com.municipalidad.backend_siam.entity.adulto_mayor;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "campanas_salud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampanaSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DATOS PRINCIPALES
    @Column(nullable = false, length = 150)
    private String nombreCampana;

    @Column(length = 120)
    private String tipoCampana;

    @Column(length = 500)
    private String descripcion;

    // UBICACIÓN
    @Column(length = 150)
    private String lugar;

    @Column(length = 120)
    private String distrito;

    @Column(length = 120)
    private String centroPoblado;

    // FECHAS
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    // RESPONSABLE
    @Column(length = 120)
    private String responsable;

    @Column(length = 120)
    private String establecimientoSalud;

    // OBJETIVO
    @Column(length = 500)
    private String objetivo;

    // SERVICIOS QUE SE BRINDARÁN
    @Column(length = 500)
    private String servicios;

    // POBLACIÓN OBJETIVO
    @Column(length = 250)
    private String poblacionObjetivo;

    // METAS
    private Integer metaAtenciones;

    @Builder.Default
    private Integer totalAtendidos = 0;

    // ESTADO: PROGRAMADA, EN_PROCESO, FINALIZADA, CANCELADA
    @Column(length = 40)
    private String estado;

    // OBSERVACIONES
    @Column(length = 500)
    private String observaciones;

    // AFICHE / IMAGEN EN BASE64
    // OJO: en PostgreSQL usa TEXT, no @Lob
    @Column(columnDefinition = "TEXT")
    private String imagen;

    @PrePersist
    public void prePersist() {

        if (estado == null || estado.isBlank()) {
            estado = "PROGRAMADA";
        }

        if (totalAtendidos == null) {
            totalAtendidos = 0;
        }

        if (metaAtenciones == null) {
            metaAtenciones = 0;
        }
    }
}