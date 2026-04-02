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
public class LoginRequest implements Serializable {
    @NotBlank(message = "L'émail est requis")
    @Email(message = "Le format est invalide")
    private String mail;

    @NotBlank(message = "Le mot de passe est requis")
    private String mdp;

}
