package org.example.anisdoufback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episode implements Serializable {
    @Id
    private Integer idE;

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
