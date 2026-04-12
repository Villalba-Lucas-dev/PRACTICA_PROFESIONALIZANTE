package com.gymApp.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id_usuario") // Une el ID con la tabla padre
public class Cliente extends Usuario {

    @Column(name = "id_entrenador")
    private Integer idEntrenador; // Permite nulos al registrarse

    @Column(name = "observaciones_medicas", length = 255)
    private String observacionesMedicas; // Le sacamos el guion bajo

    public Cliente() {
    }

    public Integer getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(Integer idEntrenador) {
        this.idEntrenador = idEntrenador;
    }

    public String getObservacionesMedicas() {
        return observacionesMedicas;
    }

    public void setObservacionesMedicas(String observacionesMedicas) {
        this.observacionesMedicas = observacionesMedicas;
    }

}