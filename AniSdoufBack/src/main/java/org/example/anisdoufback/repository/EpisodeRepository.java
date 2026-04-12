package org.example.anisdoufback.repository;

// --- Imports Projet ---
import org.example.anisdoufback.model.Episode;

// --- Imports Spring Data JPA ---
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// --- Imports Java ---
import java.util.List;

/**
 * Interface de dépôt (Repository) pour l'entité Episode.
 * Gère l'accès aux données des épisodes dans la base de données.
 */
@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    /**
     * Récupère la liste complète des épisodes rattachés à un animé spécifique.
     * Génère la requête SQL : SELECT * FROM episode WHERE idA = ?
     *
     * @param idA L'identifiant unique de l'animé parent.
     * @return Une liste d'objets Episode correspondants.
     */
    List<Episode> findByAnime_idA(Integer idA);
}
