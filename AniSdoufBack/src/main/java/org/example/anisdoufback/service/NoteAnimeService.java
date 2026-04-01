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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteAnimeService {
    private final NoteAnimeRepository noteAnimeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AnimeService animeService;

    public NoteAnimeResponse ajouterOuModifierNote(NoteAnimeRequest noteAnimeRequest){
        Utilisateur utilisateur = utilisateurRepository.findById(noteAnimeRequest.getIdU())
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

        Anime anime = animeService.getAnime(noteAnimeRequest.getIdA());

        Optional<NoteAnime> noteAnimeOptional = noteAnimeRepository
                .findByUtilisateur_IdUAndAnime_IdA(noteAnimeRequest.getIdU(), noteAnimeRequest.getIdA());

        NoteAnime noteASauvegarder;

        if (noteAnimeOptional.isPresent()){
            noteASauvegarder = noteAnimeOptional.get();
            noteASauvegarder.setNoteA(noteAnimeRequest.getNoteA());
            noteASauvegarder.setStatutA(noteAnimeRequest.getStatutA());
            noteASauvegarder.setEstFavori(noteAnimeRequest.isEstFavori());
        } else {
            noteASauvegarder = NoteAnime.builder()
                    .noteA(noteAnimeRequest.getNoteA())
                    .statutA(noteAnimeRequest.getStatutA())
                    .estFavori(noteAnimeRequest.isEstFavori())
                    .utilisateur(utilisateur)
                    .anime(anime)
                    .build();
        }

        NoteAnime noteSauvegardee = noteAnimeRepository.save(noteASauvegarder);
        return toUserResponse(noteSauvegardee);

    }

    private NoteAnimeResponse toUserResponse(NoteAnime noteAnime){
        return NoteAnimeResponse.builder()
                .idA(noteAnime.getAnime().getIdA())
                .noteA(noteAnime.getNoteA())
                .statutA(noteAnime.getStatutA() != null ? NoteAnime.StatutAnime.valueOf(noteAnime.getStatutA().name()) : null)
                .estFavori(noteAnime.isEstFavori())
                .build();
    }

}
