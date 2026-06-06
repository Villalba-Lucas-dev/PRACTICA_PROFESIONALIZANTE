package com.gymApp.backend.controllers;

import com.gymApp.backend.models.recuperacion.*;
import com.gymApp.backend.models.AuthResponseDTO;
import com.gymApp.backend.models.Cliente;
import com.gymApp.backend.models.LoginDTO;
import com.gymApp.backend.models.RegistroClienteDTO;
import com.gymApp.backend.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.gymApp.backend.services.RecuperacionService;

import java.util.Optional;

@RestController // Le dice a Spring que esta clase va a recibir peticiones de internet
@RequestMapping("/api/auth")
public class AuthController {

    //enlace a RecuperacionService
    @Autowired
    private RecuperacionService recuperacionService;

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

        // 3. Hasheamos la contraseña antes de guardarla
        String passwordEncriptada = passwordEncoder.encode(dto.password());
        nuevoCliente.setPassword(passwordEncriptada);

        // 4. Guardamos en PostgreSQL
        clienteRepository.save(nuevoCliente);

        // 5. Responderle a la App de Android (Retrofit) que salio bien
        return ResponseEntity.ok("¡Cuenta creada con éxito!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody @Valid LoginDTO dto) {

        // 1. Buscamos si existe un cliente con ese correo
        Optional<Cliente> clienteBuscado = clienteRepository.findByMail(dto.mail());

        // Si el correo no existe en la base de datos, rebotamos
        if (clienteBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Correo o contraseña incorrectos");
        }

        // 2. Extraemos el cliente real de adentro del Optional
        Cliente cliente = clienteBuscado.get();

        // Verificamos si la cuenta esta activa
        if (!cliente.isActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Tu cuenta ha sido desactivada por un administrador.");
        }

        // 3. Comparamos la contraseña que viene de Android con la encriptada en la BD
        boolean passwordCorrecta = passwordEncoder.matches(dto.password(), cliente.getPassword());

        if (passwordCorrecta) {
            // Armamos el paquete con el mensaje, el rol que tiene en la BD y su ID
            AuthResponseDTO respuesta = new AuthResponseDTO(
                    "¡Login exitoso!",
                    cliente.getRol(),
                    cliente.getIdUsuario()
            );
            return ResponseEntity.ok(respuesta);
        } else {
            // Contraseña equivocada
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Correo o contraseña incorrectos");
        }
    }

    @PostMapping("/recuperar")
    public ResponseEntity<?> recuperarPassword(
            @RequestBody RecuperacionRequest request
    ) {
        recuperacionService.enviarCodigo(request.correo());
        return ResponseEntity.ok("Código enviado al correo");
    }

}