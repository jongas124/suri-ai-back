package com.suri.suri_ai_back.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suri.suri_ai_back.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByCpf(String cpf);

    Optional<User> findByEmail(String email);

    List<User> findByNomeContainingIgnoreCase(String nome);
}
