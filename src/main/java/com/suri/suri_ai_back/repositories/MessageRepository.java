package com.suri.suri_ai_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suri.suri_ai_back.models.Message;

import java.util.UUID;


@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {}
