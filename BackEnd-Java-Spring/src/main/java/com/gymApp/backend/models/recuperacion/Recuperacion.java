package com.gymApp.backend.models.recuperacion;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recuperacion")
public class Recuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    @Column(nullable = false)
    private boolean usado = false;

    public Recuperacion() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(LocalDateTime expiracion) {
        this.expiracion = expiracion;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}