package org.example.projet_24_12.services;

import org.example.projet_24_12.entities.*;
import org.example.projet_24_12.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EleveService {

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private DossierAdministratifRepository dossierRepository;

    public List<Eleve> getAllEleves() {
        return eleveRepository.findAll();
    }

    public Optional<Eleve> getEleveById(Long id) {
        return eleveRepository.findById(id);
    }

    public Eleve saveEleve(Eleve eleve) {
        if (eleve.getFiliere() != null && eleve.getDossierAdministratif() == null) {
            DossierAdministratif dossier = new DossierAdministratif();
            String numeroInscription = generateNumeroInscription(eleve);
            dossier.setNumeroInscription(numeroInscription);
            dossier.setDateCreation(LocalDate.now());
            eleve.setDossierAdministratif(dossier);
        }
        return eleveRepository.save(eleve);
    }

    public Eleve updateEleve(Long id, Eleve eleveDetails) {
        return eleveRepository.findById(id)
                .map(eleve -> {
                    eleve.setNom(eleveDetails.getNom());
                    eleve.setPrenom(eleveDetails.getPrenom());
                    eleve.setFiliere(eleveDetails.getFiliere());

                    if (eleve.getFiliere() != null && eleve.getDossierAdministratif() != null) {
                        String nouveauNumero = generateNumeroInscription(eleve);
                        eleve.getDossierAdministratif().setNumeroInscription(nouveauNumero);
                    }

                    return eleveRepository.save(eleve);
                })
                .orElseThrow(() -> new RuntimeException("Élève non trouvé avec l'id: " + id));
    }

    public void deleteEleve(Long id) {
        eleveRepository.deleteById(id);
    }

    public Eleve inscrireCours(Long eleveId, Long coursId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        eleve.ajouterCours(cours);
        return eleveRepository.save(eleve);
    }

    public Eleve desinscrireCours(Long eleveId, Long coursId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        eleve.supprimerCours(cours);
        return eleveRepository.save(eleve);
    }

    public List<Eleve> getElevesByFiliere(Long filiereId) {
        return eleveRepository.findByFiliereId(filiereId);
    }

    public List<Eleve> searchEleves(String keyword) {
        return eleveRepository.searchByNomOrPrenom(keyword);
    }

    private String generateNumeroInscription(Eleve eleve) {
        String codeFiliere = eleve.getFiliere() != null ? eleve.getFiliere().getCode() : "GEN";
        String annee = String.valueOf(Year.now().getValue());
        String idEleve = eleve.getId() != null ? String.valueOf(eleve.getId()) : "NEW";

        return codeFiliere + "-" + annee + "-" + idEleve;
    }
}