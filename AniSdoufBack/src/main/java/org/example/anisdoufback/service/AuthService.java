package org.example.anisdoufback.service;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.TokenResponse;
import org.example.anisdoufback.dto.UtilisateurRequest;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.example.anisdoufback.config.JwtUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + mail));

        return User.builder()
                .username(utilisateur.getMail())
                .password(utilisateur.getMdp())
                .roles("USER")
                .build();
    }

    public UtilisateurResponse inscrire(UtilisateurRequest request) {
        if (utilisateurRepository.existsByMail(request.getMail())) {
            throw new IllegalArgumentException("Adresse mail déjà utilisée");
        }

        Utilisateur nouvelUtilisateur = Utilisateur.builder()
                .pseudo(request.getPseudo())
                .mail(request.getMail())
                .mdp(passwordEncoder.encode(request.getMdp())) // Hachage
                .build();

        Utilisateur utilisateurSauvegarde = utilisateurRepository.save(nouvelUtilisateur);
        return toUserResponse(utilisateurSauvegarde);
    }

    public TokenResponse login(UtilisateurRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(request.getMail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.getMdp(), utilisateur.getMdp())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(utilisateur.getMail());
        return TokenResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    private UtilisateurResponse toUserResponse(Utilisateur utilisateur){
        return UtilisateurResponse.builder()
                .idU(utilisateur.getIdU())
                .pseudo(utilisateur.getPseudo())
                .mail(utilisateur.getMail())
                .build();
    }
}