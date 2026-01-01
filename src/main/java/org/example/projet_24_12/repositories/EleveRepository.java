package org.example.projet_24_12.repositories;

import org.example.projet_24_12.entities.Eleve;
import org.example.projet_24_12.entities.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {

    List<Eleve> findByFiliereId(Long filiereId);

    @Query("SELECT e FROM Eleve e WHERE e.nom LIKE %:keyword% OR e.prenom LIKE %:keyword%")
    List<Eleve> searchByNomOrPrenom(@Param("keyword") String keyword);

}