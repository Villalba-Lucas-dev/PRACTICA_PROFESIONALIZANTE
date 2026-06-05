package com.gymApp.backend.repositories;

import com.gymApp.backend.models.recuperacion.Recuperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecuperacionRepository extends JpaRepository<Recuperacion, Integer>
{

    Optional<Recuperacion> findTopByCorreoAndCodigoAndUsadoFalseOrderByIdDesc(
            String correo,
            String codigo
    );
}