package org.example.projet_24_12.services;

import org.example.projet_24_12.entities.Cours;
import org.example.projet_24_12.entities.Filiere;
import org.example.projet_24_12.repositories.CoursRepository;
import org.example.projet_24_12.repositories.FiliereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }

    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }

    public Cours saveCours(Cours cours) {
        return coursRepository.save(cours);
    }

    public Cours updateCours(Long id, Cours coursDetails) {
        return coursRepository.findById(id)
                .map(cours -> {
                    cours.setCode(coursDetails.getCode());
                    cours.setIntitule(coursDetails.getIntitule());
                    return coursRepository.save(cours);
                })
                .orElseThrow(() -> new RuntimeException("Cours non trouvé avec l'id: " + id));
    }

    public void deleteCours(Long id) {
        coursRepository.deleteById(id);
    }

    public List<Cours> getCoursByFiliere(Long filiereId) {
        return coursRepository.findByFiliereId(filiereId);
    }

    public Cours assignerFiliere(Long coursId, Long filiereId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        cours.setFiliere(filiere);
        return coursRepository.save(cours);
    }

    public List<Cours> getCoursSansFiliere() {
        return coursRepository.findCoursSansFiliere();
    }

    public boolean existsByCode(String code) {
        return coursRepository.existsByCode(code);
    }

    public List<Cours> searchCours(String keyword) {
        return coursRepository.searchByIntitule(keyword);
    }
}