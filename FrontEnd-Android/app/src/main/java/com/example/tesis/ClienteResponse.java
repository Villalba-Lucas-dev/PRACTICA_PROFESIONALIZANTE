package com.example.tesis;

public class ClienteResponse {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String mail;
    private String rol;
    private boolean activo;
    private Integer edad;
    private String observacionesMedicas;

    // Getters
    public Integer getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getMail() { return mail; }
    public String getRol() { return rol; }
    public boolean isActivo() { return activo; }
    public Integer getEdad() { return edad; }
    public String getObservacionesMedicas() { return observacionesMedicas; }
}