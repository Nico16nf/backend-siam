package com.municipalidad.backend_siam.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // 🔥 no se envía desde frontend
    private Integer id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 60)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 60)
    private String apellidos;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String foto;

    // 🔥 CONTROL DE ROLES
    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 20)
    @Column(unique = true, nullable = false)
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    @Column(nullable = false)
    private String password;

    // 🔐 SEGURIDAD (🔥 CAMBIO IMPORTANTE)
    @Builder.Default
    private Boolean activo = true;

    @Builder.Default
    private Boolean bloqueado = false;

    @Builder.Default
    private Integer intentosFallidos = 0;

    // 🕒 AUDITORÍA
    @Column(updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fechaCreacion;

    private LocalDateTime ultimoLogin;

    // 🔥 AUTO FECHA CREACIÓN
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
}