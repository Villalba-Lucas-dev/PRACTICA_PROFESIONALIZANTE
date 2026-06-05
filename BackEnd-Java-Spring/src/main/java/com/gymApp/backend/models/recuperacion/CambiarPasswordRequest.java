package com.gymApp.backend.models.recuperacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarPasswordRequest(

        @NotBlank
        String correo,

        @NotBlank
        String codigo,

        @NotBlank
        @Size(min = 6)
        String nuevaPassword
)
{

}