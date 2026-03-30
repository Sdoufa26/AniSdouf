package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurResponse implements Serializable {
    private UUID idU;
    private String pseudo;
    private String mail;
    private Collection<NoteAnimeResponse> notesA;
    private Collection<NoteEpisodeResponse> notesE;
}
