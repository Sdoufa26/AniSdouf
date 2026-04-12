package org.example.anisdoufback.config;

// --- Imports Utilitaires ---
import lombok.RequiredArgsConstructor;
import java.util.List;

// --- Imports Spring Context et Beans ---
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// --- Imports Spring Web et CORS ---
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// --- Imports Spring Security ---
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration principale de la sécurité de l'application.
 * Gère la politique CORS, la désactivation des sessions (stateless)
 * et la protection des routes de l'API.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Récupération de l'URL du frontend autorisé depuis les properties
    @Value("${app.cors.frontend-url}")
    private String frontendUrl;

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Configure la chaîne de filtres de sécurité.
     *
     * @param http L'objet HttpSecurity permettant de construire la configuration de sécurité.
     * @return La chaîne de filtres configurée.
     * @throws Exception Si une erreur de configuration survient.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactivation de la protection CSRF et Application de la configuration CORS
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configuration de la gestion de session en mode STATELESS
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuration des autorisations pour chaque route HTTP
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Injection de notre filtre JWT personnalisé
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configure la politique CORS de l'API.
     * Détermine quels domaines, méthodes et en-têtes sont autorisés à requêter le backend.
     *
     * @return La source de configuration CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Liste des origines autorisée
        config.setAllowedOrigins(List.of(frontendUrl));
        // Liste des méthodes HTTP autorisées
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Autorise tous les en-têtes (dont Authorization)
        config.setAllowedHeaders(List.of("*"));
        // Autorise l'envoi de credentials
        config.setAllowCredentials(true);

        // Application de la configuration à l'ensemble de l'API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}