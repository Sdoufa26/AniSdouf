package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Objet de Transfert de Données (DTO) représentant le profil public de l'utilisateur.
 * Contient les informations d'identité ainsi que les statistiques agrégées pour le Dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurResponse implements Serializable {
    private UUID idU;
    private String pseudo;
    private String mail;
    private String avatar;
    private Collection<NoteAnimeResponse> notesA;
    private Collection<NoteEpisodeResponse> notesE;
    private List<NoteAnimeResponse> topAnimes;
    private long animesTermines;
    private long animesEnCours;
    private long totalRegardes;
}
