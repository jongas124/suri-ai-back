package com.suri.suri_ai_back.models.dtos;

import java.util.Set;

public record UserCreateDTO(String cpf, String nome, String email, String password, Set<String> telefones) {} 
