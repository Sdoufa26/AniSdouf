package org.example.anisdoufback.model;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

// --- Imports Jakarta ---
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entité représentant la table "Utilisateur" en base de données.
 * Gère l'identité, les informations de connexion (chiffrées) et les liens vers les notes.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idU;

    @NotBlank
    private String pseudo;

    @NotNull
    private String mail;

    @NotNull
    private String mdp;

    @Builder.Default
    @Column(name = "avatar")
    private String avatar = "avatar1.png";

    @OneToMany(mappedBy = "utilisateur")
    private Collection<NoteAnime> notesA;

    @OneToMany(mappedBy = "utilisateur")
    private Collection<NoteEpisode> notesE;

}
