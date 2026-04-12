package org.example.anisdoufback.controller;

// --- Imports Projet ---
import org.example.anisdoufback.dto.EpisodeResponse;
import org.example.anisdoufback.service.EpisodeService;

// --- Imports Spring ---
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

// --- Imports Utilitaires et Lombok ---
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST gérant la récupération des épisodes d'un animé spécifique.
 * Associe les données globales de l'épisode aux données personnelles de l'utilisateur (vu, note, favori).
 */
@RestController
@RequestMapping("/api/animes/{idA}/episodes")
@RequiredArgsConstructor
public class EpisodeController {
    private final EpisodeService episodeService;

    /**
     * Méthode GET | Récupère la liste complète des épisodes d'un animé.
     * Si les épisodes n'existent pas en base, ils sont préalablement chargés depuis l'API Jikan.
     *
     * @param idA L'identifiant de l'animé parent.
     * @return ResponseEntity contenant la liste des EpisodeResponse.
     */
    @GetMapping
    public ResponseEntity<?> recuperTousLesEpisodes(@PathVariable Integer idA){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<EpisodeResponse> maListe = episodeService.getTousLesEpisodes(idA, mail);
            return ResponseEntity.ok(maListe);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

    /**
     * Méthode GET | Récupère les détails d'un épisode spécifique.
     *
     * @param idA L'identifiant de l'animé parent.
     * @param idE L'identifiant de l'épisode ciblé.
     * @return ResponseEntity contenant les détails de l'EpisodeResponse.
     */
    @GetMapping("/{idE}")
    public ResponseEntity<?> recupererEpisodes(@PathVariable Integer idA, @PathVariable Integer idE){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            EpisodeResponse episodeResponse = episodeService.getEpisode(idA, idE, mail);
            return ResponseEntity.ok(episodeResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
