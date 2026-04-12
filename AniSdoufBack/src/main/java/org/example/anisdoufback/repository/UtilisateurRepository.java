package org.example.anisdoufback.repository;

// --- Imports Projet ---
import org.example.anisdoufback.model.Utilisateur;

// --- Imports Spring Data JPA ---
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// --- Imports Java ---
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de dépôt (Repository) pour l'entité Utilisateur.
 * Gère les opérations liées aux comptes et à l'authentification.
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    /**
     * Recherche un utilisateur par son adresse email.
     * Méthode cruciale utilisée par Spring Security pour l'authentification (login).
     *
     * @param mail L'adresse email de connexion.
     * @return Un Optional contenant l'Utilisateur s'il est trouvé.
     */
    Optional<Utilisateur> findByMail(String mail);

    /**
     * Vérifie si une adresse email est déjà enregistrée dans la base de données.
     * Utilisé lors de l'inscription pour éviter les doublons de comptes.
     *
     * @param mail L'adresse email à vérifier.
     * @return true si l'email existe déjà, false sinon.
     */
    boolean existsByMail(String mail);
}
