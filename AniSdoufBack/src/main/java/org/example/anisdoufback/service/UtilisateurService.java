package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.model.NoteAnime;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.NoteAnimeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

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

        return UtilisateurResponse.builder()
                .idU(utilisateur.getIdU())
                .mail(utilisateur.getMail())
                .pseudo(utilisateur.getPseudo())
                .animesTermines(termines)
                .animesEnCours(enCours)
                .totalRegardes(termines + enCours)
                .build();
    }
}
