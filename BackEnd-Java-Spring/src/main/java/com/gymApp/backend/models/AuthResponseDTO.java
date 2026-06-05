package com.gymApp.backend.models;

public record AuthResponseDTO(
        String mensaje,
        Rol rol,
        Integer idUsuario // Mandamos el ID por si Android lo necesita para buscar el perfil después
) {}