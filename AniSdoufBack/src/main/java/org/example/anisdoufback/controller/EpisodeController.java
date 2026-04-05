package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.model.Episode;
import org.example.anisdoufback.service.EpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/api/animes/{idA}/episodes")
@RequiredArgsConstructor
public class EpisodeController {
    private final EpisodeService episodeService;

    @GetMapping("/{idE}")
    public ResponseEntity<?> recupererEpisodes(@PathVariable Integer idA, @PathVariable Integer idE){
        try {
            Episode episode = episodeService.getEpisode(idA,idE);
            return ResponseEntity.ok(episode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
