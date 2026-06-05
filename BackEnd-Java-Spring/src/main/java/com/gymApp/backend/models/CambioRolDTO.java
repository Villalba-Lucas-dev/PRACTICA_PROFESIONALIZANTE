package com.gymApp.backend.models;

import jakarta.validation.constraints.NotNull;

public record CambioRolDTO(
        @NotNull(message = "El rol no puede estar vacío")
        Rol nuevoRol
) {}