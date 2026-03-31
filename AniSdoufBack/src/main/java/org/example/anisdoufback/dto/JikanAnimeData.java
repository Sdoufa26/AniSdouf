package org.example.anisdoufback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

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
