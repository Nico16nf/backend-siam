package com.municipalidad.backend_siam.entity.adulto_mayor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "adultos_mayores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdultoMayor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private DatosPersonales datosPersonales;

    @Embedded
    private LugarNacimiento lugarNacimiento;

    @Embedded
    private UbicacionActual ubicacionActual;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private ResponsableFamiliar responsableFamiliar;

    @Embedded
    private InformacionSocial informacionSocial;

    @Embedded
    private Sisfoh sisfoh;

    @Embedded
    private EvaluacionIntegral evaluacionIntegral;

    @Builder.Default
    private Boolean activo = true;

    private LocalDate fechaRegistro;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDate.now();

        if (this.activo == null) {
            this.activo = true;
        }
    }

    @OneToOne(mappedBy = "adultoMayor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Pension65 pension65;

    @OneToMany(mappedBy = "adultoMayor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Salud> atencionesSalud;

    @OneToMany(mappedBy = "adultoMayor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VisitaDomiciliaria> visitasDomiciliarias;

    @OneToMany(mappedBy = "adultoMayor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DerivacionSalud> derivacionesSalud;

    @JsonIgnore
    @OneToMany(mappedBy = "adultoMayor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitaMedica> visitasMedicas;
}