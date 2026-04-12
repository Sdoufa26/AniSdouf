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
 * Objet de Transfert de Données (DTO) représentant un Épisode.
 * Fusionne les données globales de l'épisode avec les données personnelles
 * de l'utilisateur connecté (vu, note, favori).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeResponse implements Serializable {
    private Integer idE;
    private String titreE;
    private Integer numero;
    private Collection<NoteEpisodeResponse> notesE;
    private Boolean estVu;
    private Double noteE;
    private Boolean estFavori;
}
