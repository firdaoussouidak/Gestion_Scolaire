package org.example.projet_24_12.services;

import org.example.projet_24_12.entities.DossierAdministratif;
import org.example.projet_24_12.repositories.DossierAdministratifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DossierAdministratifService {

    @Autowired
    private DossierAdministratifRepository dossierRepository;

    public List<DossierAdministratif> getAllDossiers() {
        return dossierRepository.findAll();
    }

    public Optional<DossierAdministratif> getDossierById(Long id) {
        return dossierRepository.findById(id);
    }

    public DossierAdministratif getDossierByNumero(String nb) {
        return dossierRepository.findByNumeroInscription(nb);
    }

    public DossierAdministratif saveDossier(DossierAdministratif dossier) {
        return dossierRepository.save(dossier);
    }

    public void deleteDossier(Long id) {
        dossierRepository.deleteById(id);
    }
}