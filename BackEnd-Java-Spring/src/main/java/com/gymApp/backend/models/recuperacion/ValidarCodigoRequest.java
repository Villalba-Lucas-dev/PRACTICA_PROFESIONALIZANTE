package com.gymApp.backend.models.recuperacion;

import jakarta.validation.constraints.NotBlank;

public record ValidarCodigoRequest(

        @NotBlank
        String correo,

        @NotBlank
        String codigo

) {
}