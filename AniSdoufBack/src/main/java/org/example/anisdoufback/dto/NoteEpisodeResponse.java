package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteEpisodeResponse implements Serializable {
    private Integer idNe;
    private int noteE;
    private boolean estFavori;
    private String statutE;
    private Integer idE;
}
