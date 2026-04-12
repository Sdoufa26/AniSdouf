package org.example.anisdoufback.controller;

// --- Imports Projet ---
import org.example.anisdoufback.dto.NoteEpisodeRequest;
import org.example.anisdoufback.dto.NoteEpisodeResponse;
import org.example.anisdoufback.service.NoteEpisodeService;

// --- Imports Spring ---
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// --- Imports Utilitaires et Lombok ---
import lombok.RequiredArgsConstructor;
import java.util.Map;

/**
 * Contrôleur REST gérant les interactions granulaires au niveau des épisodes.
 * Permet à l'utilisateur de marquer un épisode précis comme vu, de le noter ou de le mettre en favori.
 */
@RestController
@RequestMapping("/api/notes-episode")
@RequiredArgsConstructor
public class NoteEpisodeController {
    private final NoteEpisodeService noteEpisodeService;

    /**
     * Méthode POST | Sauvegarde ou met à jour les informations liées à un épisode spécifique pour l'utilisateur.
     *
     * @param noteEpisodeRequest L'objet contenant les modifications (statut, note, favori).
     * @return ResponseEntity contenant la confirmation de mise à jour.
     */
    @PostMapping
    public ResponseEntity<?> ajouterOuModifierNote(@RequestBody NoteEpisodeRequest noteEpisodeRequest){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            NoteEpisodeResponse noteEpisodeResponse = noteEpisodeService.ajouterOuModifierNote(noteEpisodeRequest, mail);
            return ResponseEntity.ok(noteEpisodeResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
