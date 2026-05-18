package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "derivaciones_salud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DerivacionSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELACIÓN
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adulto_mayor_id", nullable = false)
    private AdultoMayor adultoMayor;

    private LocalDate fechaDerivacion;

    @Column(length = 120)
    private String responsableCiam;

    @Column(length = 150)
    private String motivoDerivacion;

    @Column(length = 500)
    private String observaciones;

    // PENDIENTE - ATENDIDO - CANCELADO
    @Column(length = 40)
    private String estado;

    @Builder.Default
    private Boolean prioridadAlta = false;

    // Médico que atendió
    @Column(length = 120)
    private String medicoAsignado;

    @PrePersist
    public void prePersist() {

        if (fechaDerivacion == null) {
            fechaDerivacion = LocalDate.now();
        }

        if (estado == null) {
            estado = "PENDIENTE";
        }
    }
}