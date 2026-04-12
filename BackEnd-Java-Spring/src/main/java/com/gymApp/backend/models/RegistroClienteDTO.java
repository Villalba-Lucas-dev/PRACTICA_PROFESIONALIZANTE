package com.gymApp.backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegistroClienteDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        @NotNull(message = "La edad es obligatoria")
        Integer edad,

        @NotBlank
        @Email(message = "Formato de correo inválido")
        String mail,

        @NotBlank
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "La contraseña debe tener mín. 8 caracteres, 1 número y 1 mayúscula")
        String password
) {}