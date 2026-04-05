package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteAnimeRequest;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.example.anisdoufback.service.NoteAnimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notes-anime")
@RequiredArgsConstructor
public class NoteAnimeController {
    private final NoteAnimeService noteAnimeService;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping
    public ResponseEntity<?> ajouterOuModifierNote(@RequestBody NoteAnimeRequest noteAnimeRequest){
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Utilisateur utilisateur = utilisateurRepository.findByMail(email)
                    .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
            noteAnimeRequest.setIdU(utilisateur.getIdU());
            NoteAnimeResponse noteAnimeResponse = noteAnimeService.ajouterOuModifierNote(noteAnimeRequest);
            return ResponseEntity.ok(noteAnimeResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

}
