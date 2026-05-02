package com.gymApp.backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank @Email String mail,
        @NotBlank String password
) {}