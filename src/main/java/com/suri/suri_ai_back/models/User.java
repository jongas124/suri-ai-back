package com.suri.suri_ai_back.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "cpf", length = 16, unique = true, updatable = false, nullable = false)
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cpf;

    @Column(name = "nome", nullable = false)
    @NotBlank
    private String nome;

    @ElementCollection
    @CollectionTable(name = "user_telefones")
    private Set<String> telefones = new HashSet<String>();

    @Column(name = "email", unique = true, nullable = false, updatable = false)
    @NotBlank
    @Size(min = 5)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @OneToMany(mappedBy = "user", fetch = jakarta.persistence.FetchType.LAZY)
    private Set<Chat> chats = new HashSet<Chat>();
}
