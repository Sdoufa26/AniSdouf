package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;

/**
 * Objet de Transfert de Données (DTO) renvoyé après une connexion réussie.
 * Contient le token JWT que le frontend devra stocker et renvoyer à chaque requête.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse implements Serializable {
    private String accessToken;
    private String tokenType;
}
