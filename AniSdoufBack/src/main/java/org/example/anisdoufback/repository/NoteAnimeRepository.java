package org.example.anisdoufback.repository;

// --- Imports Projet ---
import org.example.anisdoufback.model.NoteAnime;

// --- Imports Spring Data JPA ---
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// --- Imports Java ---
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de dépôt (Repository) pour la table d'association NoteAnime.
 * Gère les requêtes liées à "Ma Liste" et aux statistiques des utilisateurs.
 */
@Repository
public interface NoteAnimeRepository extends JpaRepository<NoteAnime, Integer> {
    /**
     * Recherche une note spécifique croisant un utilisateur et un animé.
     * Utile pour vérifier si un utilisateur a déjà ajouté cet animé à sa liste.
     * Génère la requête SQL : SELECT * FROM note_anime WHERE idU = ? AND idA = ?
     *
     * @param idU L'identifiant (UUID) de l'utilisateur.
     * @param idA L'identifiant de l'animé.
     * @return Un Optional contenant la NoteAnime si elle existe, sinon vide.
     */
    Optional<NoteAnime> findByUtilisateur_IdUAndAnime_IdA(UUID idU, Integer idA);

    /**
     * Compte le nombre d'animés possédant un statut spécifique pour un utilisateur donné.
     * Utilisé pour les statistiques du Dashboard (ex: nombre d'animés "TERMINEE").
     *
     * @param idU L'identifiant de l'utilisateur.
     * @param statutA Le statut à rechercher (A_VOIR, EN_COURS, TERMINEE).
     * @return Le nombre total (long) d'animés correspondants.
     */
    long countByUtilisateur_IdUAndStatutA(UUID idU, NoteAnime.StatutAnime statutA);

    /**
     * Récupère l'intégralité de la liste ("Ma Liste") d'un utilisateur.
     *
     * @param idU L'identifiant de l'utilisateur.
     * @return La liste complète de ses suivis d'animés.
     */
    List<NoteAnime> findByUtilisateur_IdU(UUID idU);

    /**
     * Récupère les 3 animés les mieux notés par l'utilisateur pour alimenter le Panthéon.
     * Filtre les animés non notés (NULL) et trie par note décroissante (DESC).
     * Génère la requête SQL : SELECT * FROM note_anime WHERE idU = ? AND noteA IS NOT NULL ORDER BY noteA DESC LIMIT 3
     *
     * @param idU L'identifiant de l'utilisateur.
     * @return Une liste contenant au maximum les 3 meilleures NoteAnime.
     */
    List<NoteAnime> findTop3ByUtilisateur_IdUAndNoteAIsNotNullOrderByNoteADesc(UUID idU);
}
