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
public class NoteAnimeResponse implements Serializable {
    private Integer idNa;
    private int noteA;
    private boolean estFavori;
    private String statutA;
    private Integer idA;
}
