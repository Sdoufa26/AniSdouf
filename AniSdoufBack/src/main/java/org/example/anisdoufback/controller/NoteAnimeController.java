package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteAnimeRequest;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.service.NoteAnimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes-anime")
@RequiredArgsConstructor
public class NoteAnimeController {
    private final NoteAnimeService noteAnimeService;

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
