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
import org.example.anisdoufback.model.NoteEpisode;

/**
 * Objet de Transfert de Données (DTO) pour la requête d'ajout ou de mise à jour
 * des informations spécifiques à un seul épisode.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteEpisodeRequest implements Serializable {
    private Double noteE;
    private Boolean estFavori;
    private NoteEpisode.StatutEpisode statutE;
    private UUID idU;
    private Integer idE;
    private Integer idA;
}
