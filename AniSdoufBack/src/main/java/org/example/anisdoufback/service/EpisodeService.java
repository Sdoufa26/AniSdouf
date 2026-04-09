package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.EpisodeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.Episode;
import org.example.anisdoufback.model.NoteEpisode;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.EpisodeRepository;
import org.example.anisdoufback.repository.NoteEpisodeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final AnimeService animeService;
    private final UtilisateurRepository utilisateurRepository;
    private final NoteEpisodeRepository noteEpisodeRepository;

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

    public Episode getEpisodeEntity(Integer idA, Integer idE) {
        Optional<Episode> episodeOptional = episodeRepository.findById(idE);
        if (episodeOptional.isPresent()) {
            return episodeOptional.get();
        }
        chargerEpisodesDepuisJikan(idA);
        return episodeRepository.findById(idE)
                .orElseThrow(() -> new RuntimeException("L'épisode " + idE + " est introuvable"));
    }

    public EpisodeResponse getEpisode(Integer idA, Integer idE, String mail) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Episode episodeTrouve = getEpisodeEntity(idA, idE);
        return toUserResponse(episodeTrouve, utilisateur);
    }

    private EpisodeResponse toUserResponse(Episode episode, Utilisateur utilisateur) {
        Optional<NoteEpisode> noteOpt = noteEpisodeRepository.findByUtilisateur_IdUAndEpisode_IdE(utilisateur.getIdU(), episode.getIdE());
        Boolean estVu = false;
        Integer noteE = null;
        if(noteOpt.isPresent()) {
            estVu = noteOpt.get().getStatutE() == NoteEpisode.StatutEpisode.TERMINEE;
            noteE = noteOpt.get().getNoteE();
        }
        return EpisodeResponse.builder()
                .idE(episode.getIdE())
                .titreE(episode.getTitreE())
                .numero(episode.getNumero())
                .estVu(estVu)
                .noteE(noteE)
                .build();
    }
}
