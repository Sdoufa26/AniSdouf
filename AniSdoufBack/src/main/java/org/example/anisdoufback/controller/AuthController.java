package org.example.anisdoufback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.LoginRequest;
import org.example.anisdoufback.dto.RegisterRequest;
import org.example.anisdoufback.dto.TokenResponse;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> inscrire(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            UtilisateurResponse utilisateurResponse = authService.inscrire(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            TokenResponse utilisateurResponse = authService.login(loginRequest);
            return ResponseEntity.ok(utilisateurResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erreur", e.getMessage()));
        }
    }
}
