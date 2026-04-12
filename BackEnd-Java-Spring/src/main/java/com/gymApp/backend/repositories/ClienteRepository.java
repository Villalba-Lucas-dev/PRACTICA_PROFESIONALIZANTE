package com.gymApp.backend.repositories;

import com.gymApp.backend.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // "SELECT * FROM usuarios WHERE mail = ?"
    boolean existsByMail(String mail);
}