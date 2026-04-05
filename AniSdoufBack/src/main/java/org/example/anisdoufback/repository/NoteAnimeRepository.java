package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.NoteAnime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteAnimeRepository extends JpaRepository<NoteAnime, Integer> {
    Optional<NoteAnime> findByUtilisateur_IdUAndAnime_IdA(UUID idU, Integer idA);

    long countUtilisateur_IdUAndStatutA(UUID idU, NoteAnime.StatutAnime statutA);

    List<NoteAnime> findByUtilisateur_IdU(UUID idU);
}
