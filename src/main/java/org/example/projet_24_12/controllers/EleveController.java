package org.example.projet_24_12.controllers;

import org.example.projet_24_12.entities.Eleve;
import org.example.projet_24_12.entities.Filiere;
import org.example.projet_24_12.entities.Cours;
import org.example.projet_24_12.services.EleveService;
import org.example.projet_24_12.services.FiliereService;
import org.example.projet_24_12.services.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/eleves")
public class EleveController {

    @Autowired
    private EleveService eleveService;

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private CoursService coursService;

    @GetMapping
    public String getAllEleves(Model model) {
        model.addAttribute("eleves", eleveService.getAllEleves());
        return "eleves/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("eleve", new Eleve());
        model.addAttribute("filieres", filiereService.getAllFilieres());
        model.addAttribute("coursList", coursService.getAllCours());
        return "eleves/add";
    }

    @PostMapping("/add")
    public String addEleve(@ModelAttribute Eleve eleve,
                           @RequestParam(value = "coursIds", required = false) List<Long> coursIds) {
        Eleve savedEleve = eleveService.saveEleve(eleve);

        if (coursIds != null && !coursIds.isEmpty()) {
            for (Long coursId : coursIds) {
                eleveService.inscrireCours(savedEleve.getId(), coursId);
            }
        }
        return "redirect:/eleves";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Eleve eleve = eleveService.getEleveById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));

        model.addAttribute("eleve", eleve);
        model.addAttribute("filieres", filiereService.getAllFilieres());
        model.addAttribute("coursList", coursService.getAllCours());
        model.addAttribute("coursSuivisIds", eleve.getCoursSuivis().stream()
                .map(Cours::getId)
                .toList());

        return "eleves/edit";
    }

    @PostMapping("/update/{id}")
    public String updateEleve(@PathVariable Long id,
                              @ModelAttribute Eleve eleve,
                              @RequestParam(value = "coursIds", required = false) List<Long> coursIds) {
        eleveService.updateEleve(id, eleve);
        Eleve existingEleve = eleveService.getEleveById(id).orElseThrow();

        List<Long> existingCoursIds = existingEleve.getCoursSuivis().stream()
                .map(Cours::getId)
                .toList();

        for (Long coursId : existingCoursIds) {
            eleveService.desinscrireCours(id, coursId);
        }

        if (coursIds != null) {
            for (Long coursId : coursIds) {
                eleveService.inscrireCours(id, coursId);
            }
        }
        return "redirect:/eleves";
    }

    @GetMapping("/delete/{id}")
    public String deleteEleve(@PathVariable Long id) {
        eleveService.deleteEleve(id);
        return "redirect:/eleves";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        Eleve eleve = eleveService.getEleveById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));
        model.addAttribute("eleve", eleve);
        return "eleves/details";
    }

    @GetMapping("/search")
    public String searchEleves(@RequestParam("keyword") String keyword, Model model) {
        List<Eleve> eleves = eleveService.searchEleves(keyword);
        model.addAttribute("eleves", eleves);
        model.addAttribute("keyword", keyword);
        return "eleves/list";
    }

    @GetMapping("/filiere/{filiereId}")
    public String getElevesByFiliere(@PathVariable Long filiereId, Model model) {
        List<Eleve> eleves = eleveService.getElevesByFiliere(filiereId);
        Filiere filiere = filiereService.getFiliereById(filiereId).orElse(null);

        model.addAttribute("eleves", eleves);
        model.addAttribute("filiere", filiere);
        return "eleves/list";
    }
}