package com.gymApp.backend.controllers;

import com.gymApp.backend.models.CambioRolDTO;
import com.gymApp.backend.models.Cliente;
import com.gymApp.backend.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminController {

    @Autowired
    private ClienteRepository clienteRepository;

    // 1. Obtener la lista de todos los usuarios para armar el RecyclerView en Android
    @GetMapping
    public ResponseEntity<List<Cliente>> listarUsuarios() {
        return ResponseEntity.ok(clienteRepository.findAll()); // Devuelve un JSON con el array de todos los clientes registrados
    }

    // 2. Modificar el rol de un usuario específico
    @PutMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(@PathVariable Integer id, @RequestBody @Valid CambioRolDTO dto) {
        Optional<Cliente> clienteBuscado = clienteRepository.findById(id);

        if (clienteBuscado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = clienteBuscado.get();
        cliente.setRol(dto.nuevoRol()); // Actualizamos el rol con el dato del DTO
        clienteRepository.save(cliente); // Sobreescribimos el dato en PostgreSQL

        return ResponseEntity.ok("Rol actualizado con éxito a: " + dto.nuevoRol());
    }

    // 3. Eliminación lógica (Desactivar cuenta)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Integer id) {
        Optional<Cliente> clienteBuscado = clienteRepository.findById(id);

        if (clienteBuscado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = clienteBuscado.get();
        cliente.setActivo(false); // Pasamos el booleano a false
        clienteRepository.save(cliente);

        return ResponseEntity.ok("Cuenta desactivada correctamente");
    }

    // 4. Reactivar cuenta
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivarUsuario(@PathVariable Integer id) {
        Optional<Cliente> clienteBuscado = clienteRepository.findById(id);

        if (clienteBuscado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = clienteBuscado.get();
        cliente.setActivo(true); // Volvemos el booleano a true
        clienteRepository.save(cliente);

        return ResponseEntity.ok("Cuenta reactivada correctamente");
    }
}