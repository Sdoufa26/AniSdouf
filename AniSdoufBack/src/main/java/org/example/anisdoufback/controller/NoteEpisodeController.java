package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteEpisodeRequest;
import org.example.anisdoufback.dto.NoteEpisodeResponse;
import org.example.anisdoufback.service.NoteEpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notes-episode")
@RequiredArgsConstructor
public class NoteEpisodeController {
    private final NoteEpisodeService noteEpisodeService;

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
