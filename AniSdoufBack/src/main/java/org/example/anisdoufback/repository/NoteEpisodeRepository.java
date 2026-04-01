package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.NoteEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteEpisodeRepository extends JpaRepository<NoteEpisode, Integer> {
    Optional<NoteEpisode> findByUtilisateur_IdUAndEpisode_IdE(UUID idU, Integer idE);
}
