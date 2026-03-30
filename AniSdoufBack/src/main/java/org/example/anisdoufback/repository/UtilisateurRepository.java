package org.example.anisdoufback.repository;

import org.example.anisdoufback.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByMail(String mail);
}
