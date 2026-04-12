package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.Data;

// --- Imports Java ---
import java.util.List;

// --- Imports Jackson (JSON) ---
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Objet de Transfert de Données (DTO) utilisé pour mapper la réponse de l'API externe Jikan.
 * Contient les informations brutes d'un animé fournies par MyAnimeList.
 */
@Data
public class JikanAnimeData {
    @JsonProperty("mal_id")
    private Integer malId;
    private String title;
    private String synopsis;
    private Integer episodes;
    private List<JikanGenre> genres;
    private JikanImage images;

    @Data
    public static class JikanImage {
        private JikanImageJpg jpg;
    }

    @Data
    public static class JikanImageJpg {
        @JsonProperty("image_url")
        private String imageUrl;
    }

    @Data
    public static class JikanGenre {
        private String name;
    }

}
