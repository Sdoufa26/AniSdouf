package org.example.anisdoufback.model;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;
import java.util.Collection;

// --- Imports Jakarta ---
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

// --- Imports Jackson (JSON) ---
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entité représentant la table "Episode" en base de données.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idE;

    @NotNull
    private String titreE;

    @NotNull
    private Integer numero;

    @ManyToOne
    @JoinColumn(name = "idA")
    private Anime anime;

    @JsonIgnore
    @OneToMany(mappedBy = "episode")
    private Collection<NoteEpisode> notesE;
}
