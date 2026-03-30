package org.example.anisdoufback.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Anime implements Serializable {
    @Id
    private Integer idA;

    @NotNull
    private String titreA;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String genre;

    @NotNull
    private String image;

    @OneToMany(mappedBy = "anime")
    private Collection<Episode> episodes;

    @OneToMany(mappedBy = "anime")
    private Collection<NoteAnime> notesA;
}
