package org.example.anisdoufback.controller;

// --- Imports Projet ---
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.service.UtilisateurService;

// --- Imports Spring ---
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

// --- Imports Utilitaires et Lombok ---
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UtilisateurService utilisateurService;

    /**
     * Méthode GET | Récupère la liste de tous les utilisateurs du site.
     * Nécessite le rôle ADMIN.
     */
    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurResponse>> getAllUtilisateurs() {
        List<UtilisateurResponse> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }
}
