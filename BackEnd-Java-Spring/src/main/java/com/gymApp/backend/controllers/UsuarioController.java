package com.gymApp.backend.controllers;

import com.gymApp.backend.models.ActualizarPerfilDTO;
import com.gymApp.backend.models.Cliente;
import com.gymApp.backend.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMiPerfil(@PathVariable Integer id) {
        Optional<Cliente> clienteBuscado = clienteRepository.findById(id);

        if (clienteBuscado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clienteBuscado.get());
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Integer id, @RequestBody @Valid ActualizarPerfilDTO dto) {

        Optional<Cliente> clienteBuscado = clienteRepository.findById(id);

        if (clienteBuscado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = clienteBuscado.get();

        // Actualizamos solo los datos permitidos (el correo, contraseña y rol no se tocan)
        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setEdad(dto.edad());
        cliente.setObservacionesMedicas(dto.observacionesMedicas());

        clienteRepository.save(cliente); // Guardamos en PostgreSQL

        return ResponseEntity.ok("Perfil actualizado con éxito");
    }
}