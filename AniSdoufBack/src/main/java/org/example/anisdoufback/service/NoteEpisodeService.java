package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteEpisodeRequest;
import org.example.anisdoufback.dto.NoteEpisodeResponse;
import org.example.anisdoufback.model.Episode;
import org.example.anisdoufback.model.NoteEpisode;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.NoteEpisodeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteEpisodeService {
    private final NoteEpisodeRepository noteEpisodeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EpisodeService episodeService;

    public NoteEpisodeResponse ajouterOuModifierNote(NoteEpisodeRequest noteEpisodeRequest){
        Utilisateur utilisateur = utilisateurRepository.findById(noteEpisodeRequest.getIdU())
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

        Episode episode = episodeService.getEpisode(noteEpisodeRequest.getIdE(), noteEpisodeRequest.getIdA());

        Optional <NoteEpisode> noteEpisodeOptional = noteEpisodeRepository
                .findByUtilisateur_IdUAndEpisode_IdE(noteEpisodeRequest.getIdU(), noteEpisodeRequest.getIdE());

        NoteEpisode noteESauvegarder;

        if (noteEpisodeOptional.isPresent()){
            noteESauvegarder = noteEpisodeOptional.get();
            noteESauvegarder.setNoteE(noteEpisodeRequest.getNoteE());
            noteESauvegarder.setEstFavori(noteEpisodeRequest.isEstFavori());
            noteESauvegarder.setStatutE(noteEpisodeRequest.getStatutE());
        } else {
            noteESauvegarder = NoteEpisode.builder()
                    .noteE(noteEpisodeRequest.getNoteE())
                    .estFavori(noteEpisodeRequest.isEstFavori())
                    .statutE(noteEpisodeRequest.getStatutE())
                    .utilisateur(utilisateur)
                    .episode(episode)
                    .build();
        }

        NoteEpisode noteESauvegardee = noteEpisodeRepository.save(noteESauvegarder);
        return toUserResponse(noteESauvegardee);
    }

    private NoteEpisodeResponse toUserResponse(NoteEpisode noteEpisode){
        return NoteEpisodeResponse.builder()
                .idE(noteEpisode.getIdNe())
                .idU(noteEpisode.getUtilisateur().getIdU())
                .idE(noteEpisode.getEpisode().getIdE())
                .noteE(noteEpisode.getNoteE())
                .estFavori(noteEpisode.isEstFavori())
                .statutE(noteEpisode.getStatutE())
                .build();
    }
}
