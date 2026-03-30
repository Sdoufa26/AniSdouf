package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.NoteAnime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteAnimeRepository extends JpaRepository<NoteAnime, Integer> {
}
