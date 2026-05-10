package com.gymApp.backend.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigo(String destino, String codigo) {

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(destino);
        mensaje.setSubject("Codigo de Recuperación");
        mensaje.setText("Código: " + codigo);

        mailSender.send(mensaje);
    }
}