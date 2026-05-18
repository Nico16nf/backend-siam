package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.municipalidad.backend_siam.entity.adulto_mayor.enums.ClasificacionSocioeconomica;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sisfoh {

    // =====================================================
    // 🔷 INFORMACIÓN ECONÓMICA
    // =====================================================

    @Builder.Default
    private Boolean tieneIngresos = false;

    private Double ingresoMensual;

    @Column(length = 100)
    private String ocupacion;

    // =====================================================
    // 🔷 VIVIENDA
    // =====================================================

    @Column(length = 80)
    private String tipoVivienda;

    @Builder.Default
    private Boolean viviendaPropia = false;

    @Builder.Default
    private Boolean tieneAgua = false;

    @Builder.Default
    private Boolean tieneLuz = false;

    @Builder.Default
    private Boolean tieneDesague = false;

    // =====================================================
    // 🔷 CONDICIÓN FAMILIAR
    // =====================================================

    @Builder.Default
    private Boolean viveSolo = false;

    private Integer cantidadDependientes;

    @Builder.Default
    private Boolean recibeApoyoFamiliar = false;

    // =====================================================
    // 🔷 SALUD Y VULNERABILIDAD
    // =====================================================

    @Builder.Default
    private Boolean discapacidad = false;

    @Builder.Default
    private Boolean enfermedadCronica = false;

    @Builder.Default
    private Boolean situacionAbandono = false;

    // =====================================================
    // 🔷 CLASIFICACIÓN SISFOH
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ClasificacionSocioeconomica clasificacionSocioeconomica;

    @Builder.Default
    private Boolean sisfohVigente = false;

    private Integer puntajeSisfoh;

    private LocalDate fechaEvaluacion;

    private LocalDate fechaVencimiento;

    @Column(length = 500)
    private String observaciones;
}