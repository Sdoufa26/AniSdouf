package org.example.anisdoufback.model;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;

// --- Imports Jakarta ---
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Entité d'association représentant le suivi granulaire d'un Épisode par un Utilisateur.
 * Permet de savoir si un épisode précis a été vu, liké ou noté.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteEpisode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNe;

    @Min(0)
    @Max(10)
    private Double noteE;

    private Boolean estFavori;

    @Enumerated(EnumType.STRING)
    private StatutEpisode statutE;

    @ManyToOne
    @JoinColumn(name = "idU")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "idE")
    private Episode episode;

    public enum StatutEpisode {
        A_VOIR, EN_COURS, TERMINEE
    }

}
