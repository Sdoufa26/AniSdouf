package org.example.anisdoufback.dto;

// --- Imports Lombok ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Imports Java ---
import java.io.Serializable;

// --- Imports Jakarta ---
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Objet de Transfert de Données (DTO) gérant la requête de connexion d'un utilisateur.
 * Intègre les validations de format directement sur les champs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest implements Serializable {
    @NotBlank(message = "L'émail est requis")
    @Email(message = "Le format est invalide")
    private String mail;

    @NotBlank(message = "Le mot de passe est requis")
    private String mdp;

}
