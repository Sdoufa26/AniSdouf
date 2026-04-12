package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;
import java.util.Collection;

/**
 * Objet de Transfert de Données (DTO) représentant la réponse complète d'un Animé.
 * Regroupe les informations générales de l'animé ainsi que ses épisodes.
 */
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
