package org.example.anisdoufback.config;

// --- Imports Jakarta ---
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// --- Imports Utilitaires ---
import lombok.RequiredArgsConstructor;
import java.io.IOException;

// --- Imports Spring Security ---
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// --- Imports Spring Web ---
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtre de sécurité interceptant chaque requête HTTP pour vérifier la présence
 * et la validité d'un token JWT dans l'en-tête "Authorization".
 * Hérite de OncePerRequestFilter pour garantir une seule exécution par requête.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Méthode principale du filtre exécutée à chaque requête.
     * Analyse l'en-tête, valide le token et authentifie l'utilisateur dans le contexte de sécurité.
     *
     * @param request La requête HTTP entrante.
     * @param response La réponse HTTP sortante.
     * @param filterChain La chaîne de filtres de sécurité à poursuivre.
     * @throws ServletException En cas d'erreur liée aux servlets.
     * @throws IOException En cas d'erreur d'entrée/sortie lors du traitement.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Récupération de l'en-tête contenant le token
        String authHeader = request.getHeader("Authorization");

        // Vérification si l'en-tête est absent ou mal formaté
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraction du token (on ignore les 7 premiers caractères : "Bearer ")
        String token = authHeader.substring(7);

        try {
            // Tentative d'extraction de l'email depuis le token
            String email = jwtUtil.extractEmail(token);

            // Vérifie si l'email existe et que l'utilisateur n'est pas déjà authentifié dans le contexte
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Si le token est valide pour cet utilisateur, on force l'authentification
                if (jwtUtil.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token invalide ou expiré
        }
        // Poursuite de la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}