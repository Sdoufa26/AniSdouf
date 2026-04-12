package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.anisdoufback.model.NoteEpisode;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteEpisodeRequest implements Serializable {
    private Double noteE;
    private Boolean estFavori;
    private NoteEpisode.StatutEpisode statutE;
    private UUID idU;
    private Integer idE;
    private Integer idA;
}
