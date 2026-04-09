package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.model.NoteAnime;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.NoteAnimeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final NoteAnimeRepository noteAnimeRepository;

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


    public UtilisateurResponse updateAvatar(String email, String nouvelAvatar) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        utilisateur.setAvatar(nouvelAvatar);
        utilisateurRepository.save(utilisateur);

        return getMonProfil(email);
    }

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
