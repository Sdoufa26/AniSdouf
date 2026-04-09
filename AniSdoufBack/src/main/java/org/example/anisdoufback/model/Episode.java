package org.example.anisdoufback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
