package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.JikanAnimeData;
import org.example.anisdoufback.dto.JikanAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.repository.AnimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
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

    public List<Anime> rechercherAnime(String titre) {
        String url = "https://api.jikan.moe/v4/anime?q=" + titre + "&limit=10";
        RestTemplate restTemplate = new RestTemplate();
        List<Anime> resultats = new ArrayList<>();

        try {
            // 1. On interroge l'API Jikan
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            // 2. On vérifie si on a bien reçu des données
            if (response != null && response.has("data")) {
                for (JsonNode node : response.get("data")) {
                    Anime anime = new Anime();

                    // 3. Mapping des données obligatoires
                    anime.setIdA(node.get("mal_id").asInt());
                    anime.setTitreA(node.get("title").asText());

                    // Mapping des données optionnelles (Jikan renvoie parfois null)
                    if (node.has("synopsis") && !node.get("synopsis").isNull()) {
                        anime.setDescription(node.get("synopsis").asText());
                    }
                    if (node.has("episodes") && !node.get("episodes").isNull()) {
                        anime.setNbEpisodes(node.get("episodes").asInt());
                    }
                    if (node.has("images") && node.get("images").has("jpg")) {
                        anime.setImage(node.get("images").get("jpg").get("image_url").asText());
                    }

                    // Petite astuce pour récupérer le premier genre de la liste
                    if (node.has("genres") && node.get("genres").isArray() && !node.get("genres").isEmpty()) {
                        anime.setGenre(node.get("genres").get(0).get("name").asText());
                    }

                    // 4. On sauvegarde dans notre base (le Frigo)
                    animeRepository.save(anime);

                    // 5. On ajoute à la liste des résultats
                    resultats.add(anime);
                }
            }
            return resultats;

        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de la recherche sur l'API Jikan : " + e.getMessage());
        }
    }
}
