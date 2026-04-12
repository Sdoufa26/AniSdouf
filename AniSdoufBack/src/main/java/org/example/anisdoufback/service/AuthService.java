package org.example.anisdoufback.service;

// --- Imports Projet ---
import org.example.anisdoufback.dto.*;
import org.example.anisdoufback.model.Utilisateur;
import org.example.anisdoufback.repository.UtilisateurRepository;
import org.example.anisdoufback.config.JwtUtil;

// --- Imports Spring Security ---
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// --- Imports Lombok ---
import lombok.RequiredArgsConstructor;

/**
 * Service gérant l'authentification, l'inscription et la liaison avec Spring Security.
 * Implémente UserDetailsService pour fournir les informations de connexion au contexte de sécurité.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Méthode requise par Spring Security pour charger un utilisateur par son identifiant.
     *
     * @param mail L'email utilisé comme identifiant de connexion.
     * @return UserDetails contenant les informations de base (email, mot de passe hashé, rôles).
     * @throws UsernameNotFoundException Si l'email n'existe pas en base.
     */
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

    /**
     * Gère l'inscription d'un nouvel utilisateur (création de compte).
     * Vérifie l'unicité de l'email et hache le mot de passe avant sauvegarde.
     *
     * @param request Les informations saisies lors de l'inscription.
     * @return Les données de base du profil créé.
     */
    public UtilisateurResponse inscrire(RegisterRequest request) {
        if (utilisateurRepository.existsByMail(request.getMail())) {
            throw new IllegalArgumentException("Adresse mail déjà utilisée");
        }

        Utilisateur nouvelUtilisateur = Utilisateur.builder()
                .pseudo(request.getPseudo())
                .mail(request.getMail())
                .mdp(passwordEncoder.encode(request.getMdp()))
                .build();

        Utilisateur utilisateurSauvegarde = utilisateurRepository.save(nouvelUtilisateur);
        return toUserResponse(utilisateurSauvegarde);
    }

    /**
     * Gère la connexion d'un utilisateur existant.
     * Vérifie la correspondance du mot de passe et génère le jeton JWT.
     *
     * @param request Les identifiants de connexion.
     * @return Un objet contenant le token JWT généré.
     */
    public TokenResponse login(LoginRequest request) {
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

    /**
     * Convertit l'entité Utilisateur en objet DTO pour le renvoi au frontend.
     *
     * @param utilisateur L'entité de l'utilisateur.
     * @return Le DTO UtilisateurResponse formaté.
     */
    private UtilisateurResponse toUserResponse(Utilisateur utilisateur){
        return UtilisateurResponse.builder()
                .idU(utilisateur.getIdU())
                .pseudo(utilisateur.getPseudo())
                .mail(utilisateur.getMail())
                .build();
    }
}