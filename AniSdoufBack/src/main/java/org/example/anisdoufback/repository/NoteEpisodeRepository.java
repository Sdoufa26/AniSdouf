package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.NoteEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface NoteEpisodeRepository extends JpaRepository<NoteEpisode, Integer> {

}
