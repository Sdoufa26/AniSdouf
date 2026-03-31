package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.JikanAnimeData;
import org.example.anisdoufback.dto.JikanAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.repository.AnimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;

    public Anime getAnime(Integer idA){
        Optional<Anime> animeOptional = animeRepository.findById(idA);
        if (animeOptional.isPresent()) {
            return animeOptional.get();
        }

        RestClient restClient = RestClient.create();
        JikanAnimeResponse reponseJikan = restClient.get()
                .uri("https://api.jikan.moe/v4/anime/" + idA)
                .retrieve()
                .body(JikanAnimeResponse.class);

        if (reponseJikan == null || reponseJikan.getData() == null) {
            throw new RuntimeException("L'animé " + idA + "est introuvable");
        }

        JikanAnimeData donneesJikan = reponseJikan.getData();

        String imageUrl = "";
        if (donneesJikan.getImages() != null && donneesJikan.getImages().getJpg() != null) {
            imageUrl = donneesJikan.getImages().getJpg().getImageUrl();
        }

        String genresStr = "";
        if (donneesJikan.getGenres() != null) {
            genresStr = donneesJikan.getGenres().stream()
                    .map(JikanAnimeData.JikanGenre::getName)
                    .collect(java.util.stream.Collectors.joining(", "));
        }

        Anime nouvelAnime = Anime.builder()
                .idA(donneesJikan.getMalId())
                .titreA(donneesJikan.getTitle())
                .description(donneesJikan.getSynopsis())
                .nbEpisodes(donneesJikan.getEpisodes())
                .genre(genresStr)
                .image(imageUrl)
                .build();

        return animeRepository.save(nouvelAnime);

    }
}
