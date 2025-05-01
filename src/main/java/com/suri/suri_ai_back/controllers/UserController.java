package com.suri.suri_ai_back.controllers;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.suri.suri_ai_back.models.User;
import com.suri.suri_ai_back.models.dtos.UserCreateDTO;
import com.suri.suri_ai_back.models.dtos.UserUpdateDTO;
import com.suri.suri_ai_back.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
@EnableMethodSecurity
public class UserController {
    
    @Autowired
    UserService userService;
    
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj) {
        User user = this.userService.fromDTO(obj);
        User newUser = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id, JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(id);
        userService.hasPermision(userId, token);
        User obj = this.userService.findById(userId);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/telefone/{id}")
    public ResponseEntity<Set<String>> findTelefone(@PathVariable String id, JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(id);
        userService.hasPermision(userId, token);
        User obj = this.userService.findById(userId);
        return ResponseEntity.ok().body(obj.getTelefones());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UserUpdateDTO dto, JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(id);
        userService.hasPermision(userId, token);
        User obj = this.userService.findById(userId);
        this.userService.update(obj, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/telefone/{id}")
    public ResponseEntity<Void> updateTelefone(@PathVariable String id, @RequestBody Set<String> telefones, JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(id);
        userService.hasPermision(userId, token);
        User obj = this.userService.findById(userId);
        this.userService.addTelefones(obj.getId(), telefones);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(id);
        userService.hasPermision(userId, token);
        User obj = this.userService.findById(userId);
        this.userService.deleteUser(obj.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/telefone/{id}")
    public ResponseEntity<Void> deleteTelefone(@PathVariable String id, @RequestBody Set<String> telefones, JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(id);
        userService.hasPermision(userId, token);
        User obj = this.userService.findById(userId);
        this.userService.deleteTelefones(obj.getId(), telefones);
        return ResponseEntity.noContent().build();
    }
        
}