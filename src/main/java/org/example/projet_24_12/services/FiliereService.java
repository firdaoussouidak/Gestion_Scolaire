package org.example.projet_24_12.services;

import org.example.projet_24_12.entities.Filiere;
import org.example.projet_24_12.repositories.FiliereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FiliereService {

    @Autowired
    private FiliereRepository filiereRepository;

    public List<Filiere> getAllFilieres() {
        return filiereRepository.findAll();
    }

    public Optional<Filiere> getFiliereById(Long id) {
        return filiereRepository.findById(id);
    }

    public Filiere getFiliereByCode(String code) {
        return filiereRepository.findByCode(code);
    }

    public Filiere saveFiliere(Filiere filiere) {
        return filiereRepository.save(filiere);
    }

    public Filiere updateFiliere(Long id, Filiere filiereDetails) {
        return filiereRepository.findById(id)
                .map(filiere -> {
                    filiere.setCode(filiereDetails.getCode());
                    filiere.setNom(filiereDetails.getNom());
                    return filiereRepository.save(filiere);
                })
                .orElseThrow(() -> new RuntimeException("Filière non trouvée avec l'id: " + id));
    }

    public void deleteFiliere(Long id) {
        filiereRepository.deleteById(id);
    }

    public boolean existsByCode(String code) {
        return filiereRepository.existsByCode(code);
    }
}