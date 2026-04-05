package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteEpisodeRequest;
import org.example.anisdoufback.dto.NoteEpisodeResponse;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.example.anisdoufback.service.NoteEpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/api/notes-episode")
@RequiredArgsConstructor
public class NoteEpisodeController {
    private final NoteEpisodeService noteEpisodeService;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping
    public ResponseEntity<?> ajouterOuModifierNote(@RequestBody NoteEpisodeRequest noteEpisodeRequest){
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Utilisateur utilisateur = utilisateurRepository.findByMail(email)
                    .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
            noteEpisodeRequest.setIdU(utilisateur.getIdU());
            NoteEpisodeResponse noteEpisodeResponse = noteEpisodeService.ajouterOuModifierNote(noteEpisodeRequest);
            return ResponseEntity.ok(noteEpisodeResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
