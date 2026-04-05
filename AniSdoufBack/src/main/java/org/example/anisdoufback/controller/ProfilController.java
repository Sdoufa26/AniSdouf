package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/api/profil")
@RequiredArgsConstructor
public class ProfilController {
    private final UtilisateurRepository utilisateurRepository;

    @GetMapping
    public ResponseEntity<?> getMonProfil(){
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Utilisateur utilisateur = utilisateurRepository.findByMail(email)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
            UtilisateurResponse utilisateurResponse = UtilisateurResponse.builder()
                    .idU(utilisateur.getIdU())
                    .mail(utilisateur.getMail())
                    .pseudo(utilisateur.getPseudo())
                    .build();
            return ResponseEntity.ok(utilisateurResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }
}
