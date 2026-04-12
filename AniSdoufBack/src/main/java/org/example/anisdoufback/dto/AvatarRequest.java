package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;

/**
 * Objet de Transfert de Données (DTO) pour la requête de mise à jour de l'avatar.
 * Envoyé par le client lors du changement de l'image de profil.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarRequest implements Serializable {
    private String avatar;
}
