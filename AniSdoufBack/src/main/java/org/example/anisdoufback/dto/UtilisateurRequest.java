package org.example.anisdoufback.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurRequest implements Serializable {
    @NotBlank(message = "Veuillez saisir un pseudo")
    private String pseudo;

    @NotBlank(message = "Veuillez saisir une adresse mail")
    @Email(message = "Le format de l'adresse est invalide")
    private String mail;

    @NotBlank(message = "Veuillez saisir un mot de passe")
    private String mdp;
}
