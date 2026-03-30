package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    List<Episode> findByAnime_idA(Integer idA);
}
