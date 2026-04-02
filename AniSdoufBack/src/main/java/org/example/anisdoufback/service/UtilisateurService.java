package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    // Code métier prochainement

    private UtilisateurResponse toUserResponse(Utilisateur utilisateur){
        return UtilisateurResponse.builder()
                .idU(utilisateur.getIdU())
                .pseudo(utilisateur.getPseudo())
                .mail(utilisateur.getMail())
                .build();
    }
}
