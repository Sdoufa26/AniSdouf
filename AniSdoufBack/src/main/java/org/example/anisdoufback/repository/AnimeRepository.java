package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {
}
