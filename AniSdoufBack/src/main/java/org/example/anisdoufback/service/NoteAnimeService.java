package org.example.anisdoufback.service;

// --- Imports Projet ---
import org.example.anisdoufback.dto.NoteAnimeRequest;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.NoteAnime;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.AnimeRepository;
import org.example.anisdoufback.repository.NoteAnimeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;

// --- Imports Spring ---
import org.springframework.stereotype.Service;

// --- Imports Java ---
import java.util.List;

// --- Imports Lombok ---
import lombok.RequiredArgsConstructor;

/**
 * Service gérant la collection "Ma Liste" de l'utilisateur.
 * Centralise les actions d'ajout, de mise à jour des notes globales et de suivi des statuts.
 */
@Service
@RequiredArgsConstructor
public class NoteAnimeService {
    private final NoteAnimeRepository noteAnimeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AnimeRepository animeRepository;
    private final AnimeService animeService;

    /**
     * Ajoute ou met à jour la ligne de suivi (NoteAnime) d'un utilisateur pour un animé.
     * Cette méthode gère intelligemment la création (INSERT) ou la modification (UPDATE).
     *
     * @param noteAnimeRequest Les nouvelles données de suivi.
     * @param mail L'email de l'utilisateur connecté.
     * @return La note formatée après sauvegarde.
     */
    public NoteAnimeResponse ajouterOuModifierNote(NoteAnimeRequest noteAnimeRequest, String mail){
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

        Anime anime = animeRepository.findById(noteAnimeRequest.getIdA())
                .orElseThrow(() -> new RuntimeException("Animé introuvable"));

        NoteAnime noteAnime = noteAnimeRepository.findByUtilisateur_IdUAndAnime_IdA(utilisateur.getIdU(), anime.getIdA())
                .orElse(new NoteAnime());

        noteAnime.setUtilisateur(utilisateur);
        noteAnime.setAnime(anime);
        noteAnime.setStatutA(noteAnimeRequest.getStatutA());
        noteAnime.setEstFavori(noteAnimeRequest.getEstFavori());
        noteAnime.setNoteA(noteAnimeRequest.getNoteA());

        noteAnimeRepository.save(noteAnime);
        return toUserResponse(noteAnime);
    }

    /**
     * Récupère la totalité des animés ajoutés à "Ma Liste" par l'utilisateur.
     *
     * @param mail L'email de l'utilisateur.
     * @return La liste complète formatée pour le Frontend.
     */
    public List<NoteAnimeResponse> getMaListe(String mail){
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
        List<NoteAnime> maListe = noteAnimeRepository.findByUtilisateur_IdU(utilisateur.getIdU());
        return maListe.stream()
                .map(this::toUserResponse)
                .toList();
    }

    /**
     * Transforme l'entité NoteAnime en DTO en y aplatissant les informations de l'animé (Titre, Image).
     *
     * @param noteAnime L'entité de noteAnime.
     * @return Le DTO NoteAnimeResponse formaté.
     */
    private NoteAnimeResponse toUserResponse(NoteAnime noteAnime){
        return NoteAnimeResponse.builder()
                .idNa(noteAnime.getIdNa())
                .idA(noteAnime.getAnime().getIdA())
                .noteA(noteAnime.getNoteA())
                .statutA(noteAnime.getStatutA())
                .estFavori(noteAnime.getEstFavori())
                .episodesVus(noteAnime.getEpisodesVus())
                .titreA(noteAnime.getAnime().getTitreA())
                .image(noteAnime.getAnime().getImage())
                .nbEpisodes(noteAnime.getAnime().getNbEpisodes())
                .build();
    }

}
