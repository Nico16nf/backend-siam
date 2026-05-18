package com.municipalidad.backend_siam.entity.adulto_mayor;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "responsables_familiares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsableFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // 🔷 DATOS PERSONALES
    // =====================================================

    @Column(nullable = false, length = 8)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(nullable = false, length = 100)
    private String apellidoMaterno;

    @Column(length = 20)
    private String celular;

    @Column(length = 120)
    private String correo;

    // =====================================================
    // 🔷 INFORMACIÓN FAMILIAR
    // =====================================================

    @Column(length = 80)
    private String parentesco;

    private Boolean viveConAdultoMayor;

    // =====================================================
    // 🔷 UBICACIÓN
    // =====================================================

    @Embedded
    private UbicacionActual ubicacionActual;
}