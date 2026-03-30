package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeResponse implements Serializable {
    private Integer idE;
    private String titreE;
    private Integer numero;
    private Collection<NoteEpisodeResponse> notesE;

}
