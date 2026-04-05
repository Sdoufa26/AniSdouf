package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.EpisodeResponse;
import org.example.anisdoufback.dto.JikanAnimeData;
import org.example.anisdoufback.dto.JikanAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.Episode;
import org.example.anisdoufback.repository.EpisodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final AnimeService animeService;

    public List<EpisodeResponse> getTousLesEpisodes(Integer idA) {
        Anime animeParent = animeService.getAnime(idA);
        String url = "https://api.jikan.moe/v4/anime/" + idA + "/episodes";
        RestTemplate restTemplate = new RestTemplate();
        List<Episode> listeEpisodes = new ArrayList<>();

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.has("data")) {
                for (JsonNode node : response.get("data")) {
                    Integer idE = node.get("mal_id").asInt();
                    Optional<Episode> existant = episodeRepository.findById(idE);

                    if (existant.isPresent()) {
                        listeEpisodes.add(existant.get());
                    } else {
                        Episode nouvelEp = Episode.builder()
                                .idE(idE)
                                .titreE(node.has("title") && !node.get("title").isNull() ? node.get("title").asText() : "Épisode " + idE)
                                .numero(idE)
                                .anime(animeParent)
                                .build();
                        episodeRepository.save(nouvelEp);
                        listeEpisodes.add(nouvelEp);
                    }
                }
            }

            return listeEpisodes.stream().map(this::toUserResponse).toList();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des épisodes : " + e.getMessage());
        }
    }

    public Episode getEpisodeEntity(Integer idA, Integer idE) {
        Optional<Episode> episodeOptional = episodeRepository.findById(idE);

        if (episodeOptional.isPresent()) {
            return episodeOptional.get();
        }

        getTousLesEpisodes(idA);

        return episodeRepository.findById(idE)
                .orElseThrow(() -> new RuntimeException("L'épisode " + idE + " est introuvable"));
    }

    public EpisodeResponse getEpisode(Integer idA, Integer idE) {
        Optional<Episode> episodeOptional = episodeRepository.findById(idE);


        if (episodeOptional.isPresent()) {
            return toUserResponse(episodeOptional.get());
        }

        getTousLesEpisodes(idA);

        Episode episodeTrouve = episodeRepository.findById(idE)
                .orElseThrow(() -> new RuntimeException("L'épisode " + idE + " est introuvable même après téléchargement"));

        return toUserResponse(episodeTrouve);
    }

    private EpisodeResponse toUserResponse(Episode episode) {
        return EpisodeResponse.builder()
                .idE(episode.getIdE())
                .titreE(episode.getTitreE())
                .numero(episode.getNumero())
                .build();
    }
}
