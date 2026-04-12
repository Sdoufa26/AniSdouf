package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;

// --- Imports Projet ---
import org.example.anisdoufback.model.NoteAnime;

/**
 * Objet de Transfert de Données (DTO) représentant une ligne dans la "Ma Liste" de l'utilisateur.
 * Aplatit les données de l'animé (titre, image) et les données de suivi (statut, note)
 * pour faciliter l'affichage côté Frontend.
 */
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
    private String genre;
}
