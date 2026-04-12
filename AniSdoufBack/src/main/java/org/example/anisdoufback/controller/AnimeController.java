package org.example.anisdoufback.controller;

// --- Imports Projet ---
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.service.AnimeService;

// --- Imports Spring ---
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// --- Imports Utilitaires et Lombok ---
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST gérant les requêtes liées au catalogue global des animés.
 * Fournit les endpoints pour la recherche, la consultation et les suggestions.
 */
@RestController
@RequestMapping("/api/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;

    /**
     * Méthode GET | Récupère les détails d'un animé spécifique via son identifiant.
     *
     * @param idA L'identifiant unique de l'animé (API Jikan).
     * @return ResponseEntity contenant l'objet Anime, ou un message d'erreur (400) si introuvable.
     */
    @GetMapping("/{idA}")
    public ResponseEntity<?> recupererAnime(@PathVariable Integer idA){
        try {
            Anime anime = animeService.getAnime(idA);
            return ResponseEntity.ok(anime);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

    /**
     * Méthode GET | Recherche une liste d'animés correspondant à un titre donné.
     *
     * @param titre Le titre ou mot-clé à rechercher.
     * @return ResponseEntity contenant une liste d'animés correspondants.
     */
    @GetMapping("/recherche")
    public ResponseEntity<?> rechercherAnime(@RequestParam String titre){
        try {
            List<Anime> resultats = animeService.rechercherAnime(titre);
            return ResponseEntity.ok(resultats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

    /**
     * Méthode GET | Génère une liste de suggestions d'animés personnalisée pour l'utilisateur connecté.
     *
     * @return ResponseEntity contenant la liste des suggestions.
     */
    @GetMapping("/suggestions")
    public ResponseEntity<?> getSuggestions(){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Anime> suggestions = animeService.getSuggestions(mail);
            return ResponseEntity.ok(suggestions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
