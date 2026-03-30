package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Utilisateur findByMail(String mail);
}
