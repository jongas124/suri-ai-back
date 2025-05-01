package com.suri.suri_ai_back.models.dtos;

import java.util.Set;

public record UserUpdateDTO(String nome, String password, Set<String> telefones) {}
