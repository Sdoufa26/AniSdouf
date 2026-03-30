package org.example.anisdoufback.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
public class Utilisateur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idU;

    @NotNull
    private String pseudo;

    @NotNull
    private String mail;

    @NotNull
    private String mdp;

    @OneToMany(mappedBy = "utilisateur")
    private Collection<NoteAnime> animes;

    @OneToMany(mappedBy = "utilisateur")
    private Collection<NoteEpisode> episodes;

}
