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
 * Objet de Transfert de Données (DTO) confirmant l'enregistrement
 * de la note d'un épisode au frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteEpisodeResponse implements Serializable {
    private Integer idNe;
    private Double noteE;
    private Boolean estFavori;
    private NoteEpisode.StatutEpisode statutE;
    private UUID idU;
    private Integer idE;
}
