package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.anisdoufback.model.NoteAnime;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteAnimeResponse implements Serializable {
    private Integer idNa;
    private Double noteA;
    private Boolean estFavori;
    private Integer episodesVus;
    private NoteAnime.StatutAnime statutA;
    private Integer idA;
    private String titreA;
    private String image;
    private Integer nbEpisodes;
}
