package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.JikanAnimeData;
import org.example.anisdoufback.dto.JikanAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.repository.AnimeRepository;
import org.example.anisdoufback.repository.NoteAnimeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;
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
    private final UtilisateurRepository utilisateurRepository;
    private final NoteAnimeRepository noteAnimeRepository;

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
        String url = "https://api.jikan.moe/v4/anime?q=" + titre + "&limit=10&type=tv&order_by=members&sort=desc";
        RestTemplate restTemplate = new RestTemplate();
        List<Anime> resultats = new ArrayList<>();

        try {
            // 1. On interroge l'API Jikan
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            // 2. On vérifie si on a bien reçu des données
            if (response != null && response.has("data")) {
                for (JsonNode node : response.get("data")) {

                    // Filtre du titre de l'animé
                    String titrePrincipal = node.get("title").asText();
                    String titreAnglais = (node.has("title_english") && !node.get("title_english").isNull())
                            ? node.get("title_english").asText()
                            : "";

                    // Si la recherche n'est ni dans le titre principal, ni dans le titre anglais, on l'ignore !
                    if (!titrePrincipal.toLowerCase().contains(titre.toLowerCase()) &&
                            !titreAnglais.toLowerCase().contains(titre.toLowerCase())) {
                        continue; // On passe au résultat suivant sans l'ajouter
                    }
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

    public List<Anime> getSuggestions(String mail) {
        // 1. On récupère l'utilisateur
        org.example.anisdoufback.model.Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 2. On récupère les IDs des animés qu'il a DÉJÀ dans sa liste
        List<Integer> idsDejaDansListe = noteAnimeRepository.findByUtilisateur_IdU(utilisateur.getIdU())
                .stream()
                .map(note -> note.getAnime().getIdA())
                .toList();

        // 3. On appelle le Top 20 de Jikan
        String url = "https://api.jikan.moe/v4/top/anime?filter=bypopularity&limit=20";
        RestTemplate restTemplate = new RestTemplate();
        List<Anime> suggestions = new ArrayList<>();

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("data")) {
                for (JsonNode node : response.get("data")) {
                    int idA = node.get("mal_id").asInt();

                    // 🛑 LE FILTRE MAGIQUE : Si l'utilisateur l'a déjà, on passe au suivant !
                    if (idsDejaDansListe.contains(idA)) {
                        continue;
                    }

                    Anime anime = new Anime();
                    anime.setIdA(idA);
                    anime.setTitreA(node.get("title").asText());

                    if (node.has("synopsis") && !node.get("synopsis").isNull()) {
                        anime.setDescription(node.get("synopsis").asText());
                    }
                    if (node.has("episodes") && !node.get("episodes").isNull()) {
                        anime.setNbEpisodes(node.get("episodes").asInt());
                    }
                    if (node.has("images") && node.get("images").has("jpg")) {
                        anime.setImage(node.get("images").get("jpg").get("image_url").asText());
                    }
                    if (node.has("genres") && node.get("genres").isArray() && !node.get("genres").isEmpty()) {
                        anime.setGenre(node.get("genres").get(0).get("name").asText());
                    }

                    animeRepository.save(anime);
                    suggestions.add(anime);

                    // On s'arrête dès qu'on a 10 suggestions parfaites
                    if (suggestions.size() >= 10) break;
                }
            }
            return suggestions;
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de la récupération des suggestions : " + e.getMessage());
        }
    }
}
