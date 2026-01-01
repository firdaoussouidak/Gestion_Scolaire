package org.example.projet_24_12.repositories;

import org.example.projet_24_12.entities.Cours;
import org.example.projet_24_12.entities.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    List<Cours> findByFiliere(Filiere filiere);

    List<Cours> findByFiliereId(Long filiereId);

    boolean existsByCode(String code);

    @Query("SELECT c FROM Cours c WHERE c.filiere IS NULL")
    List<Cours> findCoursSansFiliere();

    @Query("SELECT c FROM Cours c WHERE c.intitule LIKE %:keyword% OR c.code LIKE %:keyword%")
    List<Cours> searchByIntitule(@Param("keyword") String keyword);
}