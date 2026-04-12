package org.example.anisdoufback.service;

// --- Imports Projet ---
import org.example.anisdoufback.dto.EpisodeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.Episode;
import org.example.anisdoufback.model.NoteEpisode;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.EpisodeRepository;
import org.example.anisdoufback.repository.NoteEpisodeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;

// --- Imports Spring ---
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// --- Imports Java ---
import java.util.List;
import java.util.Optional;

// --- Imports Lombok ---
import lombok.RequiredArgsConstructor;

// --- Imports Jackson (JSON)
import tools.jackson.databind.JsonNode;

/**
 * Service responsable de la gestion des épisodes.
 * Interagit avec l'API Jikan pour hydrater la base de données si nécessaire,
 * et croise les données des épisodes avec les interactions de l'utilisateur (vu, note, favori).
 */
@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final AnimeService animeService;
    private final UtilisateurRepository utilisateurRepository;
    private final NoteEpisodeRepository noteEpisodeRepository;

    /**
     * Charge l'intégralité des épisodes d'un animé depuis l'API externe Jikan et les sauvegarde.
     * Cette méthode est appelée automatiquement si l'animé ne possède pas encore ses épisodes en local.
     *
     * @param idA L'identifiant de l'animé.
     */
    private void chargerEpisodesDepuisJikan(Integer idA) {
        Anime animeParent = animeService.getAnime(idA);
        String url = "https://api.jikan.moe/v4/anime/" + idA + "/episodes";
        RestTemplate restTemplate = new RestTemplate();
        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("data")) {
                for (JsonNode node : response.get("data")) {
                    int numeroEp = node.get("mal_id").asInt();
                    Episode episode = new Episode();
                    episode.setTitreE(node.get("title").asText());
                    episode.setNumero(numeroEp);
                    episode.setAnime(animeParent);
                    episodeRepository.save(episode);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des épisodes : " + e.getMessage());
        }
    }

    /**
     * Récupère la liste de tous les épisodes d'un animé avec l'état personnel de l'utilisateur.
     *
     * @param idA L'identifiant de l'animé.
     * @param mail L'email de l'utilisateur effectuant la requête.
     * @return La liste des épisodes formatés en DTO.
     */
    public List<EpisodeResponse> getTousLesEpisodes(Integer idA, String mail) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Episode> listeEpisodes = episodeRepository.findByAnime_idA(idA);
        if (listeEpisodes.isEmpty()) {
            chargerEpisodesDepuisJikan(idA);
            listeEpisodes = episodeRepository.findByAnime_idA(idA);
        }

        return listeEpisodes.stream().map(ep -> toUserResponse(ep, utilisateur)).toList();
    }

    /**
     * Méthode interne pour récupérer une entité Episode en s'assurant qu'elle existe.
     *
     * @param idA L'identifiant de l'animé.
     * @param idE L'identifiant de l'épisode.
     * @return L'entité de l'épisode cherché.
     */
    public Episode getEpisodeEntity(Integer idA, Integer idE) {
        Optional<Episode> episodeOptional = episodeRepository.findById(idE);
        if (episodeOptional.isPresent()) {
            return episodeOptional.get();
        }
        chargerEpisodesDepuisJikan(idA);
        return episodeRepository.findById(idE)
                .orElseThrow(() -> new RuntimeException("L'épisode " + idE + " est introuvable"));
    }

    /**
     * Récupère un épisode spécifique formaté pour l'utilisateur.
     *
     * @param idA L'identifiant de l'animé.
     * @param idE L'identifiant de l'épisode.
     * @param mail L'émail de l'utilisateur
     * @return L'épisode recherché
     */
    public EpisodeResponse getEpisode(Integer idA, Integer idE, String mail) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Episode episodeTrouve = getEpisodeEntity(idA, idE);
        return toUserResponse(episodeTrouve, utilisateur);
    }

    /**
     * Assemble les données de l'épisode avec les interactions personnelles de l'utilisateur.
     *
     * @param episode L'entité de l'épisode.
     * @param utilisateur L'entité de l'utilisateur.
     * @return Le DTO EpisodeResponse formaté.
     */
    private EpisodeResponse toUserResponse(Episode episode, Utilisateur utilisateur) {
        Optional<NoteEpisode> noteOpt = noteEpisodeRepository.findByUtilisateur_IdUAndEpisode_IdE(utilisateur.getIdU(), episode.getIdE());
        Boolean estVu = false;
        Double noteE = null;
        Boolean estFavori = false;
        if(noteOpt.isPresent()) {
            estVu = noteOpt.get().getStatutE() == NoteEpisode.StatutEpisode.TERMINEE;
            noteE = noteOpt.get().getNoteE();
            estFavori = noteOpt.get().getEstFavori() != null ? noteOpt.get().getEstFavori() : false;
        }
        return EpisodeResponse.builder()
                .idE(episode.getIdE())
                .titreE(episode.getTitreE())
                .numero(episode.getNumero())
                .estVu(estVu)
                .noteE(noteE)
                .estFavori(estFavori)
                .build();
    }
}
