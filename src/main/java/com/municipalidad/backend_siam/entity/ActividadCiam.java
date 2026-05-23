package com.municipalidad.backend_siam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "actividades_ciam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActividadCiam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String tipo;

    private String descripcion;

    private String lugar;

    private LocalDate fecha;

    private String responsable;

    private Integer cupos;

    private Boolean activa = true;
}
