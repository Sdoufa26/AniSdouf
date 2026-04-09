package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.NoteAnimeRequest;
import org.example.anisdoufback.dto.NoteAnimeResponse;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.NoteAnime;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.AnimeRepository;
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
    private final AnimeRepository animeRepository;
    private final AnimeService animeService;

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
