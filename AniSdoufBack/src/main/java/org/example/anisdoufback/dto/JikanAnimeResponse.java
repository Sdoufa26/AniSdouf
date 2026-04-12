package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.Data;

/**
 * Objet de Transfert de Données (DTO) racine pour l'API Jikan.
 * L'API Jikan englobe toujours ses réponses dans un objet parent "data".
 */
@Data
public class JikanAnimeResponse {
    private JikanAnimeData data;
}
