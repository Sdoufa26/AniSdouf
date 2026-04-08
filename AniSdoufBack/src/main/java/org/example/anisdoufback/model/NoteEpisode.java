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
public class NoteEpisode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNe;

    @Min(0)
    @Max(10)
    private Integer noteE;

    private Boolean estFavori;

    @Enumerated(EnumType.STRING)
    private StatutEpisode statutE;

    @ManyToOne
    @JoinColumn(name = "idU")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "idE")
    private Episode episode;

    public enum StatutEpisode {
        A_VOIR, EN_COURS, TERMINEE
    }

}
