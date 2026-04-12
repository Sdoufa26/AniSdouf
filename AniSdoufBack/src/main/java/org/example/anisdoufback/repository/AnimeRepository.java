package org.example.anisdoufback.repository;

// --- Imports Projet ---
import org.example.anisdoufback.model.Anime;

// --- Imports Spring Data JPA ---
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de dépôt (Repository) pour l'entité Anime.
 * Hérite de JpaRepository pour fournir automatiquement les opérations CRUD standards
 * (save, findById, findAll, delete) sans avoir à écrire de requêtes SQL.
 */
@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {
}
