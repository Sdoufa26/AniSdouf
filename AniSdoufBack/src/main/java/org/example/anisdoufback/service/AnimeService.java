package org.example.anisdoufback.service;

// --- Imports Projet ---
import org.example.anisdoufback.dto.JikanAnimeData;
import org.example.anisdoufback.dto.JikanAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.repository.AnimeRepository;
import org.example.anisdoufback.repository.NoteAnimeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;

// --- Imports Spring ---
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

// --- Imports Java ---
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// --- Imports Lombok ---
import lombok.RequiredArgsConstructor;

// --- Imports Jackson (JSON)
import tools.jackson.databind.JsonNode;

/**
 * Service gérant la logique métier liée au catalogue d'animés.
 * Interagit avec la base de données locale et agit comme un pont vers l'API externe Jikan (MyAnimeList)
 * pour enrichir le catalogue à la volée.
 */
@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NoteAnimeRepository noteAnimeRepository;

    /**
     * Récupère un animé par son identifiant.
     * Si l'animé n'existe pas en base locale, il est récupéré depuis l'API Jikan puis sauvegardé.
     *
     * @param idA L'identifiant unique de l'animé (format MyAnimeList).
     * @return L'entité Anime correspondante.
     * @throws IllegalArgumentException Si l'animé est introuvable sur Jikan.
     */
    public Anime getAnime(Integer idA){
        // Vérification dans la base de données
        Optional<Anime> animeOptional = animeRepository.findById(idA);
        if (animeOptional.isPresent()) {
            return animeOptional.get();
        }

        // Si absent, appel à l'API externe Jikan
        RestClient restClient = RestClient.create();
        JikanAnimeResponse reponseJikan = restClient.get()
                .uri("https://api.jikan.moe/v4/anime/" + idA)
                .retrieve()
                .body(JikanAnimeResponse.class);

        if (reponseJikan == null || reponseJikan.getData() == null) {
            throw new RuntimeException("L'animé " + idA + "est introuvable");
        }

        JikanAnimeData donneesJikan = reponseJikan.getData();

        // Extraction et formatage des genres
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

        // Construction et sauvegarde de la nouvelle entité
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

    /**
     * Recherche des animés par titre via l'API Jikan.
     *
     * @param titre Le terme de recherche.
     * @return Une liste d'animés correspondants (limités à 10 par défaut).
     */
    public List<Anime> rechercherAnime(String titre) {
        String url = "https://api.jikan.moe/v4/anime?q=" + titre + "&limit=10&type=tv&order_by=members&sort=desc";
        RestTemplate restTemplate = new RestTemplate();
        List<Anime> resultats = new ArrayList<>();

        try {
            // On interroge l'API Jikan
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            // On vérifie si on a bien reçu des données
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

                    // Mapping des données obligatoires
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

                    List<String> labels = new ArrayList<>();

                    // On récupère tous les genres classiques (Action, Comédie, etc.)
                    if (node.has("genres") && node.get("genres").isArray()) {
                        for (JsonNode genreNode : node.get("genres")) {
                            labels.add(genreNode.get("name").asText());
                        }
                    }

                    // On récupère toutes les démographies (Shounen, Seinen, etc.)
                    if (node.has("demographics") && node.get("demographics").isArray()) {
                        for (JsonNode demoNode : node.get("demographics")) {
                            labels.add(demoNode.get("name").asText());
                        }
                    }

                    // On assemble le tout avec des virgules (ex: "Action, Adventure, Shounen")
                    if (!labels.isEmpty()) {
                        anime.setGenre(String.join(", ", labels));
                    } else {
                        anime.setGenre("Inconnu");
                    }

                    // On sauvegarde dans notre base
                    animeRepository.save(anime);

                    // On ajoute à la liste des résultats
                    resultats.add(anime);
                }
            }
            return resultats;

        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de la recherche sur l'API Jikan : " + e.getMessage());
        }
    }

    /**
     * Génère des suggestions basées sur le premier animé "Terminé" de la liste de l'utilisateur.
     *
     * @param mail L'email de l'utilisateur connecté.
     * @return Une liste de recommandations.
     */
    public List<Anime> getSuggestions(String mail) {
        // On récupère l'utilisateur
        org.example.anisdoufback.model.Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // On récupère les IDs des animés qu'il a DÉJÀ dans sa liste
        List<Integer> idsDejaDansListe = noteAnimeRepository.findByUtilisateur_IdU(utilisateur.getIdU())
                .stream()
                .map(note -> note.getAnime().getIdA())
                .toList();

        // On appelle le Top 20 de Jikan
        String url = "https://api.jikan.moe/v4/top/anime?filter=bypopularity&limit=20";
        RestTemplate restTemplate = new RestTemplate();
        List<Anime> suggestions = new ArrayList<>();

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("data")) {
                for (JsonNode node : response.get("data")) {
                    int idA = node.get("mal_id").asInt();

                    // Filtre : Si l'utilisateur l'a déjà, on passe au suivant
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
                    List<String> labels = new ArrayList<>();

                    // On récupère tous les genres classiques (Action, Comédie, etc.)
                    if (node.has("genres") && node.get("genres").isArray()) {
                        for (JsonNode genreNode : node.get("genres")) {
                            labels.add(genreNode.get("name").asText());
                        }
                    }

                    // On récupère toutes les démographies (Shounen, Seinen, etc.)
                    if (node.has("demographics") && node.get("demographics").isArray()) {
                        for (JsonNode demoNode : node.get("demographics")) {
                            labels.add(demoNode.get("name").asText());
                        }
                    }

                    // On assemble le tout avec des virgules (ex: "Action, Adventure, Shounen")
                    if (!labels.isEmpty()) {
                        anime.setGenre(String.join(", ", labels));
                    } else {
                        anime.setGenre("Inconnu");
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
