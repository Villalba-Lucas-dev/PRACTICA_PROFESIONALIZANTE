package com.gymApp.backend.models.recuperacion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperacionRequest(

        @NotBlank
        @Email
        String correo

)
{

}