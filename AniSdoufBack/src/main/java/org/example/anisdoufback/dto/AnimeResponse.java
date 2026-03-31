package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimeResponse implements Serializable {
    private Integer idA;
    private String titreA;
    private String description;
    private String genre;
    private String image;
    private Integer nbEpisodes;
    private Collection<EpisodeResponse> episodes;
    private Collection<NoteAnimeResponse> notesA;

}
