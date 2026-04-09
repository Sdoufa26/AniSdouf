package org.example.anisdoufback.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

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
