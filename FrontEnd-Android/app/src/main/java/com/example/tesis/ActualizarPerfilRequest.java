package com.example.tesis;

public class ActualizarPerfilRequest {
    private String nombre;
    private String apellido;
    private Integer edad;
    private String observacionesMedicas;

    public ActualizarPerfilRequest(String nombre, String apellido, Integer edad, String observacionesMedicas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.observacionesMedicas = observacionesMedicas;
    }
}