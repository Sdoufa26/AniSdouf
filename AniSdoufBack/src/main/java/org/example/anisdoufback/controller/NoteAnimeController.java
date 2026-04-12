package org.example.anisdoufback.controller;

// --- Imports Projet ---
import org.example.anisdoufback.dto.NoteAnimeRequest;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.service.NoteAnimeService;

// --- Imports Spring ---
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// --- Imports Utilitaires et Lombok ---
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST gérant la liste personnelle d'animés de l'utilisateur (Ma Liste).
 * Permet de noter, mettre en favori et changer le statut d'un animé.
 */
@RestController
@RequestMapping("/api/notes-anime")
@RequiredArgsConstructor
public class NoteAnimeController {
    private final NoteAnimeService noteAnimeService;

    /**
     * Méthode POST | Ajoute un animé à la liste de l'utilisateur ou met à jour ses informations existantes.
     * Gère les opérations d'INSERT et d'UPDATE en base de données.
     *
     * @param noteAnimeRequest Objet contenant la nouvelle configuration (note, statut, favori...).
     * @return ResponseEntity contenant la note mise à jour.
     */
    @PostMapping
    public ResponseEntity<?> ajouterOuModifierNote(@RequestBody NoteAnimeRequest noteAnimeRequest){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            NoteAnimeResponse noteAnimeResponse = noteAnimeService.ajouterOuModifierNote(noteAnimeRequest, mail);
            return ResponseEntity.ok(noteAnimeResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

    /**
     * Méthode GET | Récupère l'intégralité de la liste d'animés de l'utilisateur connecté.
     *
     * @return ResponseEntity contenant une liste d'objets NoteAnimeResponse.
     */
    @GetMapping
    public ResponseEntity<?> recupererMaListe(){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<NoteAnimeResponse> maListe = noteAnimeService.getMaListe(mail);
            return ResponseEntity.ok(maListe);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
