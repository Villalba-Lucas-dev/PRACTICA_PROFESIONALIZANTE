package com.gymApp.backend.controllers;

import com.gymApp.backend.models.Cliente;
import com.gymApp.backend.models.RegistroClienteDTO;
import com.gymApp.backend.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // Le dice a Spring que esta clase va a recibir peticiones de internet
@RequestMapping("/api/auth")
public class AuthController {

    // Inyectamos el repositorio creado para poder hablar con PostgreSQL
    @Autowired
    private ClienteRepository clienteRepository;

    // Herramienta de Spring Security para encriptar contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Esta ruta recibe peticiones POST
    @PostMapping("/registro")
    public ResponseEntity<?> registrarCliente(@RequestBody @Valid RegistroClienteDTO dto) {

        // 1. Verificamos si el correo ya existe en la Base de Datos
        if (clienteRepository.existsByMail(dto.mail())) {
            return ResponseEntity.badRequest().body("Error: El correo ya está registrado");
        }

        // 2. Pasamos los datos del DTO a la Entidad Cliente
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(dto.nombre());
        nuevoCliente.setApellido(dto.apellido());
        nuevoCliente.setEdad(dto.edad());
        nuevoCliente.setMail(dto.mail());

        // 3. ¡Hasheamos la contraseña antes de guardarla!
        String passwordEncriptada = passwordEncoder.encode(dto.password());
        nuevoCliente.setPassword(passwordEncriptada);

        // 4. Guardamos en PostgreSQL
        clienteRepository.save(nuevoCliente);

        // 5. Responderle a la App de Android (Retrofit) que todo salió perfecto
        return ResponseEntity.ok("¡Cuenta creada con éxito!");
    }
}