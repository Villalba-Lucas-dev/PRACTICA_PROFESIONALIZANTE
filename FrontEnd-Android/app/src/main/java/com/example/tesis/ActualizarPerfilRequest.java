package com.example.tesis;

public class ActualizarPerfilRequest {
    private String nombre;
    private String apellido;
    private Integer edad;

    public ActualizarPerfilRequest(String nombre, String apellido, Integer edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }
}