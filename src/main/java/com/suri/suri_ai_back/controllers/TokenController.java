package com.suri.suri_ai_back.controllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suri.suri_ai_back.models.User;
import com.suri.suri_ai_back.models.dtos.LoginRequestDTO;
import com.suri.suri_ai_back.models.dtos.LoginResponseDTO;
import com.suri.suri_ai_back.services.UserService;
import com.suri.suri_ai_back.util.Argon2Encoder;

import lombok.AllArgsConstructor;

@RestController
@Validated
@RequestMapping("/login")
@AllArgsConstructor
public class TokenController {
    private final JwtEncoder jwtEncoder;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        User user = this.userService.findByEmail(loginRequest.login());

        userService.isLoginCorrect(loginRequest, user);

        Argon2Encoder argon2Encoder = new Argon2Encoder();

        //verifica se a senha precisa ser recriptografada
        if (argon2Encoder.upgradeEncoding(user.getPassword())) {
            user.setPassword(argon2Encoder.encode(loginRequest.password()));
            this.userService.update(user, loginRequest);
        }

        Instant now = Instant.now();
        Long expiresIn = 3000L; // 3000 segundos: 50 minutos
        //String scope = user.getRole().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("Suri_AI")
            .subject(user.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .build();

        String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponseDTO(jwtValue, expiresIn));
    }

}
