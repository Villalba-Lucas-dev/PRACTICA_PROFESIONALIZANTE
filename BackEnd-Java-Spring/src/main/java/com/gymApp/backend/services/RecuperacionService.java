package com.gymApp.backend.services;

import com.gymApp.backend.models.Cliente;
import com.gymApp.backend.models.recuperacion.Recuperacion;
import com.gymApp.backend.models.EmailService;
import com.gymApp.backend.repositories.ClienteRepository;
import com.gymApp.backend.repositories.RecuperacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class RecuperacionService {

    @Autowired
    private RecuperacionRepository recuperacionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void enviarCodigo(String correo)
    {
        Optional<Cliente> cliente = clienteRepository.findByMail(correo);

        if (cliente.isEmpty()) {
            throw new RuntimeException("El correo no existe");
        }

        String codigo = generarCodigo();

        Recuperacion recuperacion = new Recuperacion();

        recuperacion.setCorreo(correo);
        recuperacion.setCodigo(codigo);
        recuperacion.setExpiracion(LocalDateTime.now().plusMinutes(10)); // ahora y 10 minutos mas

        recuperacionRepository.save(recuperacion); //se guardan los cambios en db- save = INSERT INTO

        emailService.enviarCodigo(correo, codigo);
    }

    public void validarCodigo(
            String correo,
            String codigo
    )
    {
        Recuperacion recuperacion = recuperacionRepository
                .findTopByCorreoAndCodigoAndUsadoFalseOrderByIdDesc(
                        correo, //correo siempre estaria ""bien"" bajo este modelo
                        codigo
                )
                .orElseThrow(() ->
                        new RuntimeException("Código inválido"));

        if (recuperacion.getExpiracion().isBefore(LocalDateTime.now()))
        {
            throw new RuntimeException("Código expirado");
        }
    }



    public void cambiarPassword(
            String correo,
            String codigo,
            String nuevaPassword
    ) {

        Optional<Recuperacion> recuperacionOptional =
                recuperacionRepository
                        .findTopByCorreoAndCodigoAndUsadoFalseOrderByIdDesc(
                                correo, //el correo siempre seria valido en la practica
                                codigo
                        );
        if (recuperacionOptional.isEmpty()) {
            throw new RuntimeException("Código inválido"); // ->no se hallo objeto con ese correo y codigo asignado
        } //->esto CORTA FLUJO

        Recuperacion recuperacion = recuperacionOptional.get(); //-> c toma objeto recuperacion

        if (recuperacion.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado");
        }

        Cliente cliente = clienteRepository.findByMail(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        cliente.setPassword(passwordEncoder.encode(nuevaPassword));

        //guarda el cliente y hacer insert de id de forma automatica (por eso el espacio Integer de la interfaz)
        clienteRepository.save(cliente);

        recuperacion.setUsado(true);

        recuperacionRepository.save(recuperacion);
    }


    //generacion simple del codigo (se utiliza arriba en esta misma clase)
    private String generarCodigo() {

        Random random = new Random();

        int numero = 100000 + random.nextInt(900000);

        return String.valueOf(numero);
    }
}
