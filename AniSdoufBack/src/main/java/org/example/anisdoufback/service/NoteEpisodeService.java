package org.example.anisdoufback.service;

// --- Imports Projet ---
import org.example.anisdoufback.dto.NoteEpisodeRequest;
import org.example.anisdoufback.dto.NoteEpisodeResponse;
import org.example.anisdoufback.model.Episode;
import org.example.anisdoufback.model.NoteEpisode;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.NoteEpisodeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;

// --- Imports Spring ---
import org.springframework.stereotype.Service;

// --- Imports Java ---
import java.util.Optional;

// --- Imports Lombok ---
import lombok.RequiredArgsConstructor;

/**
 * Service gérant le suivi individuel et granulaire des épisodes.
 * Permet à un utilisateur de marquer un épisode précis comme vu, de le noter ou de le mettre en favori.
 */
@Service
@RequiredArgsConstructor
public class NoteEpisodeService {
    private final NoteEpisodeRepository noteEpisodeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EpisodeService episodeService;

    /**
     * Enregistre ou met à jour l'interaction d'un utilisateur sur un épisode ciblé.
     * Fonctionne sur le principe de l'Upsert : crée la relation si elle n'existe pas (INSERT),
     * sinon la met à jour (UPDATE).
     *
     * @param noteEpisodeRequest Les données de l'interaction (statut, favori, note) envoyées par le Frontend.
     * @param mail L'email de l'utilisateur connecté.
     * @return La confirmation de l'enregistrement formatée en DTO.
     */
    public NoteEpisodeResponse ajouterOuModifierNote(NoteEpisodeRequest noteEpisodeRequest, String mail){
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

        noteEpisodeRequest.setIdU(utilisateur.getIdU());
        Episode episode = episodeService.getEpisodeEntity(noteEpisodeRequest.getIdA(), noteEpisodeRequest.getIdE());

        Optional <NoteEpisode> noteEpisodeOptional = noteEpisodeRepository
                .findByUtilisateur_IdUAndEpisode_IdE(noteEpisodeRequest.getIdU(), noteEpisodeRequest.getIdE());

        NoteEpisode noteESauvegarder;

        if (noteEpisodeOptional.isPresent()){
            noteESauvegarder = noteEpisodeOptional.get();
            noteESauvegarder.setNoteE(noteEpisodeRequest.getNoteE());
            noteESauvegarder.setEstFavori(noteEpisodeRequest.getEstFavori());
            noteESauvegarder.setStatutE(noteEpisodeRequest.getStatutE());
        } else {
            noteESauvegarder = NoteEpisode.builder()
                    .noteE(noteEpisodeRequest.getNoteE())
                    .estFavori(noteEpisodeRequest.getEstFavori())
                    .statutE(noteEpisodeRequest.getStatutE())
                    .utilisateur(utilisateur)
                    .episode(episode)
                    .build();
        }

        NoteEpisode noteESauvegardee = noteEpisodeRepository.save(noteESauvegarder);
        return toUserResponse(noteESauvegardee);
    }

    /**
     * Utilitaire de transformation : Convertit l'entité sauvegardée en DTO de réponse.
     *
     * @param noteEpisode L'entité NoteEpisode.
     * @return Le DTO NoteEpisodeResponse formaté.
     */
    private NoteEpisodeResponse toUserResponse(NoteEpisode noteEpisode){
        return NoteEpisodeResponse.builder()
                .idU(noteEpisode.getUtilisateur().getIdU())
                .idE(noteEpisode.getEpisode().getIdE())
                .noteE(noteEpisode.getNoteE())
                .estFavori(noteEpisode.getEstFavori())
                .statutE(noteEpisode.getStatutE())
                .build();
    }
}
