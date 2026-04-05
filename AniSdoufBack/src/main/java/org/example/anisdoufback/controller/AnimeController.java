package org.example.anisdoufback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteAnimeRequest;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.service.AnimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;

    @GetMapping("/{idA}")
    public ResponseEntity<?> recupererAnime(@PathVariable Integer idA){
        try {
            Anime anime = animeService.getAnime(idA);
            return ResponseEntity.ok(anime);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

    @GetMapping("/recherche")
    public ResponseEntity<?> rechercherAnime(@RequestParam String titre){
        try {
            List<Anime> resultats = animeService.rechercherAnime(titre);
            return ResponseEntity.ok(resultats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
