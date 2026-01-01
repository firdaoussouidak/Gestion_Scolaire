package org.example.projet_24_12.repositories;

import org.example.projet_24_12.entities.DossierAdministratif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DossierAdministratifRepository extends JpaRepository<DossierAdministratif, Long> {

    DossierAdministratif findByNumeroInscription(String numeroInscription);
}