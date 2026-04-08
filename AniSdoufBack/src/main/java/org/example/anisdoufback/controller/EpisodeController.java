package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.EpisodeResponse;
import org.example.anisdoufback.service.EpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animes/{idA}/episodes")
@RequiredArgsConstructor
public class EpisodeController {
    private final EpisodeService episodeService;

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
