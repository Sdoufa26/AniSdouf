package org.example.anisdoufback.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
public class Anime implements Serializable {
    @Id
    private int idA;

    @NotNull
    private String titreA;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    private String image;

    @OneToMany(mappedBy = "anime")
    private Collection<Episode> episodes;

    @OneToMany(mappedBy = "anime")
    private Collection<NoteAnime> notesA;
}
