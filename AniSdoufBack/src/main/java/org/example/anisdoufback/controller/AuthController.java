package org.example.anisdoufback.controller;

// --- Imports Projet ---
import org.example.anisdoufback.dto.LoginRequest;
import org.example.anisdoufback.dto.RegisterRequest;
import org.example.anisdoufback.dto.TokenResponse;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.service.AuthService;

// --- Imports Spring ---
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

// --- Imports Utilitaires et Lombok ---
import lombok.RequiredArgsConstructor;
import java.util.Map;

// --- Imports Validation ---
import jakarta.validation.Valid;

/**
 * Contrôleur REST gérant l'authentification et l'inscription des utilisateurs.
 * Ces endpoints sont publics (non protégés par le filtre JWT).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Méthode POST | Inscrit un nouvel utilisateur dans le système.
     *
     * @param registerRequest Objet contenant les informations d'inscription (pseudo, email, mdp).
     * @return ResponseEntity contenant les données du profil créé (201 CREATED) ou une erreur (400).
     */
    @PostMapping("/register")
    public ResponseEntity<?> inscrire(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            UtilisateurResponse utilisateurResponse = authService.inscrire(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }

    /**
     * Méthode POST | Authentifie un utilisateur existant.
     *
     * @param loginRequest Objet contenant les identifiants de connexion (email, mdp).
     * @return ResponseEntity contenant le token JWT (200 OK) ou une erreur d'autorisation (401).
     */
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
