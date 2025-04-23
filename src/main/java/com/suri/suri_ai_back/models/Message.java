package com.suri.suri_ai_back.models;

import java.time.LocalDateTime;

import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "sender", nullable = false)
    @NotBlank
    private String sender;

    @Column(name = "content", nullable = false, columnDefinition = 	"TEXT")
    @NotBlank
    private String content;

    @Column(name = "timestamp", nullable = false, updatable = false)
    @CreatedDate
    @NotNull
    private LocalDateTime timestamp;
}
