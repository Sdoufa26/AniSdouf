package org.example.anisdoufback.service;

// --- Imports Projet ---
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.model.NoteAnime;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.NoteAnimeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;

// --- Imports Spring ---
import org.springframework.stereotype.Service;

// --- Imports Java ---
import java.util.List;
import java.util.stream.Collectors;

// --- Imports Lombok ---
import lombok.RequiredArgsConstructor;

/**
 * Service gérant le compte utilisateur, son identité et l'agrégation de ses statistiques.
 * Agit comme le chef d'orchestre pour générer les données complexes du Dashboard (Statistiques + Top 3).
 */
@RequiredArgsConstructor
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final NoteAnimeRepository noteAnimeRepository;

    /**
     * Calcule et consolide toutes les informations nécessaires à l'affichage du Dashboard.
     * Effectue l'agrégation des statistiques et détermine le Top 3.
     *
     * @param email L'email de l'utilisateur cible.
     * @return DTO complet (UtilisateurResponse) contenant l'identité et les statistiques.
     */
    public UtilisateurResponse getMonProfil(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        long termines = noteAnimeRepository.countByUtilisateur_IdUAndStatutA(utilisateur.getIdU(), NoteAnime.StatutAnime.TERMINEE);
        long enCours = noteAnimeRepository.countByUtilisateur_IdUAndStatutA(utilisateur.getIdU(), NoteAnime.StatutAnime.EN_COURS);

        List<NoteAnime> top3 = noteAnimeRepository.findTop3ByUtilisateur_IdUAndNoteAIsNotNullOrderByNoteADesc(utilisateur.getIdU());
        List<NoteAnimeResponse> top3Response = top3.stream().map(this::toNoteResponse).collect(Collectors.toList());


        return UtilisateurResponse.builder()
                .idU(utilisateur.getIdU())
                .mail(utilisateur.getMail())
                .pseudo(utilisateur.getPseudo())
                .avatar(utilisateur.getAvatar())
                .animesTermines(termines)
                .animesEnCours(enCours)
                .totalRegardes(termines + enCours)
                .topAnimes(top3Response)
                .build();
    }

    /**
     * Modifie l'image de profil (avatar) de l'utilisateur.
     *
     * @param email L'email de l'utilisateur.
     * @param nouvelAvatar L'URL ou le nom de fichier de la nouvelle image.
     * @return Le profil mis à jour, reflétant le nouvel avatar.
     */
    public UtilisateurResponse updateAvatar(String email, String nouvelAvatar) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        utilisateur.setAvatar(nouvelAvatar);
        utilisateurRepository.save(utilisateur);

        return getMonProfil(email);
    }

    /**
     * Utilitaire de transformation : Convertit l'entité NoteAnime en DTO pour le Top 3.
     * @param noteAnime L'entité de noteAnime.
     * @return Le DTO NoteAnimeResponse formaté.
     */
    private NoteAnimeResponse toNoteResponse(NoteAnime noteAnime) {
        return NoteAnimeResponse.builder()
                .idNa(noteAnime.getIdNa())
                .idA(noteAnime.getAnime().getIdA())
                .noteA(noteAnime.getNoteA())
                .statutA(noteAnime.getStatutA())
                .titreA(noteAnime.getAnime().getTitreA())
                .image(noteAnime.getAnime().getImage())
                .nbEpisodes(noteAnime.getAnime().getNbEpisodes())
                .build();
    }
}
