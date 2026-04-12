package org.example.anisdoufback.config;

// --- Imports Spring ---
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// --- Imports Spring Security ---
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuration globale de l'application.
 * Contient les déclarations des Beans principaux utilisés pour la sécurité,
 * tels que l'encodeur de mots de passe et le gestionnaire d'authentification.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Définit l'algorithme de hachage utilisé pour les mots de passe.
     * BCrypt est le standard actuel recommandé par Spring Security.
     * @return Une Instance de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit le gestionnaire d'authentification de Spring Security.
     * Ce gestionnaire est utilisé pour valider les identifiants lors de la connexion.
     * @param config La configuration d'authentification injectée par Spring.
     * @return L'instance d'AuthenticationManager configurée.
     * @throws Exception Si une erreur survient lors de la récupération du gestionnaire.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}