package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;
import java.util.UUID;

// --- Imports Projet ---
import org.example.anisdoufback.model.NoteAnime;

/**
 * Objet de Transfert de Données (DTO) pour la requête d'ajout ou de mise à jour
 * des informations de suivi d'un animé par l'utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteAnimeRequest implements Serializable {
    private Double noteA;
    private NoteAnime.StatutAnime statutA;
    private Boolean estFavori;
    private Integer episodesVus;
    private UUID idU;
    private Integer idA;
}
