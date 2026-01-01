package org.example.projet_24_12.controllers;

import org.example.projet_24_12.entities.DossierAdministratif;
import org.example.projet_24_12.entities.Eleve;
import org.example.projet_24_12.services.DossierAdministratifService;
import org.example.projet_24_12.services.EleveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Controller
@RequestMapping("/dossiers")
public class DossierAdministratifController {

    @Autowired
    private DossierAdministratifService dossierService;

    @Autowired
    private EleveService eleveService;

    @GetMapping
    public String getAllDossiers(Model model) {
        List<DossierAdministratif> dossiers = dossierService.getAllDossiers();
        model.addAttribute("dossiers", dossiers);
        return "dossiers/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("dossier", new DossierAdministratif());
        model.addAttribute("eleves", eleveService.getAllEleves());
        model.addAttribute("currentYear", Year.now().getValue());
        return "dossiers/add";
    }

    @PostMapping("/add")
    public String addDossier(@ModelAttribute DossierAdministratif dossier,
                             @RequestParam("eleveId") Long eleveId) {
        Eleve eleve = eleveService.getEleveById(eleveId)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));

        if (dossier.getNumeroInscription() == null || dossier.getNumeroInscription().isEmpty()) {
            String codeFiliere = eleve.getFiliere() != null ? eleve.getFiliere().getCode() : "GEN";
            String annee = String.valueOf(Year.now().getValue());
            String idEleve = eleve.getId() != null ? String.valueOf(eleve.getId()) : "NEW";
            dossier.setNumeroInscription(codeFiliere + "-" + annee + "-" + idEleve);
        }

        if (dossier.getDateCreation() == null) {
            dossier.setDateCreation(LocalDate.now());
        }

        dossier.setEleve(eleve);
        eleve.setDossierAdministratif(dossier);

        dossierService.saveDossier(dossier);
        eleveService.saveEleve(eleve);

        return "redirect:/dossiers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DossierAdministratif dossier = dossierService.getDossierById(id)
                .orElseThrow(() -> new RuntimeException("Dossier non trouvé"));

        model.addAttribute("dossier", dossier);
        model.addAttribute("eleves", eleveService.getAllEleves());
        model.addAttribute("currentYear", Year.now().getValue());
        return "dossiers/edit";
    }

    @PostMapping("/update/{id}")
    public String updateDossier(@PathVariable Long id,
                                @ModelAttribute DossierAdministratif dossier,
                                @RequestParam("eleveId") Long eleveId) {
        DossierAdministratif existingDossier = dossierService.getDossierById(id)
                .orElseThrow(() -> new RuntimeException("Dossier non trouvé"));

        Eleve oldEleve = existingDossier.getEleve();
        Eleve newEleve = eleveService.getEleveById(eleveId)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));

        if (oldEleve != null) {
            oldEleve.setDossierAdministratif(null);
            eleveService.saveEleve(oldEleve);
        }

        existingDossier.setNumeroInscription(dossier.getNumeroInscription());
        existingDossier.setDateCreation(dossier.getDateCreation());
        existingDossier.setEleve(newEleve);

        newEleve.setDossierAdministratif(existingDossier);

        dossierService.saveDossier(existingDossier);
        eleveService.saveEleve(newEleve);

        return "redirect:/dossiers";
    }

    @GetMapping("/delete/{id}")
    public String deleteDossier(@PathVariable Long id) {
        dossierService.deleteDossier(id);
        return "redirect:/dossiers";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        DossierAdministratif dossier = dossierService.getDossierById(id)
                .orElseThrow(() -> new RuntimeException("Dossier non trouvé"));

        model.addAttribute("dossier", dossier);
        return "dossiers/details";
    }

    @GetMapping("/search")
    public String searchDossiers(@RequestParam("numero") String numero, Model model) {
        DossierAdministratif dossier = dossierService.getDossierByNumero(numero);
        if (dossier != null) {
            model.addAttribute("dossiers", List.of(dossier));
        } else {
            model.addAttribute("dossiers", List.of());
        }
        model.addAttribute("numero", numero);
        return "dossiers/list";
    }
}