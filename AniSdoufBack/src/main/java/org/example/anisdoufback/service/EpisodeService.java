package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.JikanAnimeData;
import org.example.anisdoufback.dto.JikanAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.Episode;
import org.example.anisdoufback.repository.EpisodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final AnimeService animeService;

    public Episode getEpisode(Integer idE, Integer idA){
        Optional<Episode> episodeOptional = episodeRepository.findById(idE);
        if (episodeOptional.isPresent()){
            return episodeOptional.get();
        }

        RestClient restClient = RestClient.create();
        JikanAnimeResponse reponseJikan = restClient.get()
                .uri("https://api.jikan.moe/v4/anime/" + idA + "episodes/" + idE)
                .retrieve()
                .body(JikanAnimeResponse.class);

        if (reponseJikan == null || reponseJikan.getData() == null){
            throw new RuntimeException("L'épisode est introuvable");
        }

        JikanAnimeData donneesJikan = reponseJikan.getData();
        Anime animeParent = animeService.getAnime(idA);


        Episode nouvelEpisode = Episode.builder()
                .idE(donneesJikan.getMalId())
                .titreE(donneesJikan.getTitle())
                .numero(donneesJikan.getEpisodes())
                .anime(animeParent)
                .build();

        return episodeRepository.save(nouvelEpisode);
    }
}
