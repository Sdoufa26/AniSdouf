package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteAnimeRequest;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.NoteAnime;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.NoteAnimeRepository;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteAnimeService {
    private final NoteAnimeRepository noteAnimeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AnimeService animeService;

    public NoteAnimeResponse ajouterOuModifierNote(NoteAnimeRequest noteAnimeRequest, String mail){
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

        noteAnimeRequest.setIdU(utilisateur.getIdU());
        Anime anime = animeService.getAnime(noteAnimeRequest.getIdA());

        Optional<NoteAnime> noteAnimeOptional = noteAnimeRepository
                .findByUtilisateur_IdUAndAnime_IdA(noteAnimeRequest.getIdU(), noteAnimeRequest.getIdA());

        NoteAnime noteASauvegarder;

        if (noteAnimeOptional.isPresent()){
            noteASauvegarder = noteAnimeOptional.get();
            noteASauvegarder.setNoteA(noteAnimeRequest.getNoteA());
            noteASauvegarder.setStatutA(noteAnimeRequest.getStatutA());
            noteASauvegarder.setEstFavori(noteAnimeRequest.getEstFavori());
            noteASauvegarder.setEpisodesVus(noteAnimeRequest.getEpisodesVus());
        } else {
            noteASauvegarder = NoteAnime.builder()
                    .noteA(noteAnimeRequest.getNoteA())
                    .statutA(noteAnimeRequest.getStatutA())
                    .estFavori(noteAnimeRequest.getEstFavori())
                    .episodesVus(noteAnimeRequest.getEpisodesVus())
                    .utilisateur(utilisateur)
                    .anime(anime)
                    .build();
        }

        NoteAnime noteSauvegardee = noteAnimeRepository.save(noteASauvegarder);
        return toUserResponse(noteSauvegardee);
    }

    public List<NoteAnimeResponse> getMaListe(String mail){
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
        List<NoteAnime> maListe = noteAnimeRepository.findByUtilisateur_IdU(utilisateur.getIdU());
        return maListe.stream()
                .map(this::toUserResponse)
                .toList();
    }

    private NoteAnimeResponse toUserResponse(NoteAnime noteAnime){
        return NoteAnimeResponse.builder()
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
