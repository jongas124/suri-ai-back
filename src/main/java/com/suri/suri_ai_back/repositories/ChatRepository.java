package com.suri.suri_ai_back.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suri.suri_ai_back.models.Chat;

public interface ChatRepository extends JpaRepository<Chat, UUID> {}
