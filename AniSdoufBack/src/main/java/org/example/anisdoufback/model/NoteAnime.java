package org.example.anisdoufback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private Integer noteA;

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
