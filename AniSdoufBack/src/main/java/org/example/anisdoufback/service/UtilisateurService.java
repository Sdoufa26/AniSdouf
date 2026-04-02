package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.UtilisateurRequest;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurResponse iscrireUtilisateur(UtilisateurRequest utilisateurRequest){
        if (utilisateurRepository.existsByMail(utilisateurRequest.getMail())) {
            throw new RuntimeException("Adresse mail déja utilisée");
        }

        Utilisateur nouvelUtilisateur = Utilisateur.builder()
                .pseudo(utilisateurRequest.getPseudo())
                .mail(utilisateurRequest.getMail())
                .mdp(utilisateurRequest.getMdp())
                .build();

        Utilisateur utilisateurInscris = utilisateurRepository.save(nouvelUtilisateur);
        return toUserResponse(utilisateurInscris);
    }

    public UtilisateurResponse loginUtilisateur(UtilisateurRequest utilisateurRequest){
        Utilisateur utilisateur = utilisateurRepository.findByMail(utilisateurRequest.getMail())
                .orElseThrow(()-> new RuntimeException("Adresse mail introuvable"));

        if (!(utilisateurRequest.getMdp().equals(utilisateur.getMdp())))
            throw new RuntimeException("Mot de passe incorrect");

        return toUserResponse(utilisateur);
    }

    private UtilisateurResponse toUserResponse(Utilisateur utilisateur){
        return UtilisateurResponse.builder()
                .idU(utilisateur.getIdU())
                .pseudo(utilisateur.getPseudo())
                .mail(utilisateur.getMail())
                .build();
    }
}
