package com.gymApp.backend.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActualizarPerfilDTO(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "El apellido no puede estar vacío")
        String apellido,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 15, message = "La edad mínima permitida es 15 años")
        @Max(value = 100, message = "La edad máxima permitida es 100 años")
        Integer edad,
        String observacionesMedicas
) {}