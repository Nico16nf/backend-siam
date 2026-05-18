package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pension_65")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pension65 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELACIÓN CON ADULTO MAYOR
    @OneToOne
    @JoinColumn(name = "adulto_mayor_id", nullable = false, unique = true)

    // EVITA BUCLE INFINITO JSON
    @JsonBackReference
    private AdultoMayor adultoMayor;

    // RESULTADO PRINCIPAL
    @Builder.Default
    private Boolean beneficiario = false;

    @Builder.Default
    private Boolean posibleBeneficiario = false;

    // CRITERIOS DE EVALUACIÓN
    @Builder.Default
    private Boolean cumpleEdad = false;

    @Builder.Default
    private Boolean cumpleDni = false;

    @Builder.Default
    private Boolean cumpleSisfoh = false;

    @Builder.Default
    private Boolean noRecibeOtraPension = false;

    @Builder.Default
    private Boolean tieneFormulario1000 = false;

    @Builder.Default
    private Boolean tieneDiscapacidad = false;

    @Column(length = 80)
    private String estado;
    // BENEFICIARIO, POSIBLE_BENEFICIARIO, OBSERVADO,
    // NO_CALIFICA, SUSPENDIDO, ACTIVO

    @Column(length = 500)
    private String motivoEvaluacion;

    private LocalDate fechaEvaluacion;

    private LocalDate fechaAfiliacion;

    private LocalDate fechaUltimoPago;

    private LocalDate fechaProximoPago;

    @Builder.Default
    private Double montoUltimoPago = 0.0;

    @Builder.Default
    private Double montoProximoPago = 350.0;

    @Column(length = 500)
    private String observaciones;

    @PrePersist
    public void prePersist() {

        if (this.beneficiario == null) {
            this.beneficiario = false;
        }

        if (this.posibleBeneficiario == null) {
            this.posibleBeneficiario = false;
        }

        if (this.montoProximoPago == null) {
            this.montoProximoPago = 350.0;
        }
    }
}