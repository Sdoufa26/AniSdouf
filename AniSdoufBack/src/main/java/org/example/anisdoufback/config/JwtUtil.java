package org.example.anisdoufback.config;

// --- Imports JWT ---
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// --- Imports Spring ---
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

// --- Imports Java Standards ---
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Classe utilitaire pour la gestion des tokens JWT.
 * Permet la génération, l'extraction de données et la validation des tokens.
 */
@Component
public class JwtUtil {

    // Récupération de la clé secrète depuis application.properties
    @Value("${app.jwt.secret}")
    private String secret;

    // Récupération du temps d'expiration depuis application.properties
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Génère la clé de signature cryptographique basée sur le secret configuré.
     *
     * @return La clé secrète (SecretKey) utilisée pour signer le token.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Génère un nouveau token JWT pour un utilisateur donné.
     *
     * @param email L'adresse email de l'utilisateur.
     * @return Une chaîne de caractères représentant le token JWT signé.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait l'adresse email contenue dans le token JWT.
     *
     * @param token Le token JWT reçu.
     * @return L'email de l'utilisateur extrait du token.
     */
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Vérifie la validité d'un token en s'assurant qu'il appartient bien à l'utilisateur
     * et qu'il n'est pas expiré.
     *
     * @param token Le token JWT à vérifier.
     * @param userDetails Les détails de l'utilisateur récupérés en base de données.
     * @return true si le token est valide, false sinon.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Décrypte et extrait toutes les revendications contenues dans le token.
     *
     * @param token Le token JWT.
     * @return Les données contenues dans le token.
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Vérifie si le token a dépassé sa date d'expiration.
     *
     * @param token Le token JWT.
     * @return true si le token est expiré, false sinon.
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}