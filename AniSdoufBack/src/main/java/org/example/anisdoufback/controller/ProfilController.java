package org.example.anisdoufback.controller;

// --- Imports Projet ---
import org.example.anisdoufback.dto.AvatarRequest;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.service.UtilisateurService;

// --- Imports Spring ---
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// --- Imports Utilitaires et Lombok ---
import lombok.RequiredArgsConstructor;
import java.util.Map;

/**
 * Contrôleur REST gérant la consultation et la modification du profil utilisateur.
 * Utilisé principalement pour alimenter le Dashboard et gérer l'identité visuelle.
 */
@RestController
@RequestMapping("/api/profil")
@RequiredArgsConstructor
public class ProfilController {
    private final UtilisateurService utilisateurService;

    /**
     * Méthode GET | Récupère les données complètes du profil utilisateur, incluant les statistiques globales
     * (animés terminés, en cours) et le Top 3 (Panthéon).
     *
     * @return ResponseEntity contenant les données formatées du profil.
     */
    @GetMapping
    public ResponseEntity<?> getMonProfil(){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            UtilisateurResponse utilisateurResponse = utilisateurService.getMonProfil(mail);
            return ResponseEntity.ok(utilisateurResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

    /**
     * Méthode PUT | Met à jour l'avatar de l'utilisateur connecté.
     *
     * @param request L'objet contenant le chemin de la nouvelle image d'avatar sélectionnée.
     * @return ResponseEntity confirmant la mise à jour avec le profil actualisé.
     */
    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@RequestBody AvatarRequest request){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            UtilisateurResponse response = utilisateurService.updateAvatar(mail, request.getAvatar());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }
}
