package org.example.anisdoufback.repository;

// --- Imports Projet ---
import org.example.anisdoufback.model.NoteEpisode;

// --- Imports Spring Data JPA ---
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// --- Imports Java ---
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de dépôt (Repository) pour la table d'association NoteEpisode.
 * Gère le suivi individuel de chaque épisode (vu, note, favori) par les utilisateurs.
 */
@Repository
public interface NoteEpisodeRepository extends JpaRepository<NoteEpisode, Integer> {
    /**
     * Récupère le suivi spécifique d'un utilisateur pour un épisode précis.
     * Utilisé lors de l'ouverture de la modale des épisodes pour pré-remplir les données.
     * Génère la requête SQL : SELECT * FROM note_episode WHERE idU = ? AND idE = ?
     *
     * @param idU L'identifiant de l'utilisateur.
     * @param idE L'identifiant de l'épisode.
     * @return Un Optional contenant la NoteEpisode si l'utilisateur a interagi avec cet épisode.
     */
    Optional<NoteEpisode> findByUtilisateur_IdUAndEpisode_IdE(UUID idU, Integer idE);
}
