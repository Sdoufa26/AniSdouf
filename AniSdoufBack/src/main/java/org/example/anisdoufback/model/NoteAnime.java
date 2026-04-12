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
 * Entité d'association représentant la note et le statut d'un Animé pour un Utilisateur spécifique.
 * Correspond à l'entrée d'un animé dans "Ma Liste".
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteAnime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNa;

    @Min(0)
    @Max(10)
    private Double noteA;

    @Enumerated(EnumType.STRING)
    private StatutAnime statutA;

    private Boolean estFavori;

    private Integer episodesVus;

    @ManyToOne
    @JoinColumn(name = "idU")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "idA")
    private Anime anime;

    public enum StatutAnime {
        A_VOIR, EN_COURS,TERMINEE
    }

}
