package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.anisdoufback.model.Anime;
import org.example.anisdoufback.model.NoteAnime;
import org.example.anisdoufback.model.Utilisateur;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteAnimeRequest implements Serializable {
    private int noteA;
    private NoteAnime.StatutAnime statutA;
    private boolean estFavori;
    private int episodesVus;
    private UUID idU;
    private Integer idA;
}
