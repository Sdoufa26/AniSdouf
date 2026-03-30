package org.example.anisdoufback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
public class Episode implements Serializable {
    @Id
    private int idE;

    @NotNull
    private String titreE;

    @NotNull
    private String numero;

    @ManyToOne
    @JoinColumn(name = "idA")
    private Anime anime;

    @OneToMany(mappedBy = "episode")
    private Collection<NoteEpisode> notesE;
}
