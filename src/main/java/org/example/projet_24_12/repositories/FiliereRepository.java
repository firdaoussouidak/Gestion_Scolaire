package org.example.projet_24_12.repositories;

import org.example.projet_24_12.entities.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Long> {

    Filiere findByCode(String code);

    boolean existsByCode(String code);
}