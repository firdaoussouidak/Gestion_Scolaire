package org.example.projet_24_12.controllers;

import org.example.projet_24_12.entities.Cours;
import org.example.projet_24_12.services.CoursService;
import org.example.projet_24_12.services.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cours")
public class CoursController {

    @Autowired
    private CoursService coursService;

    @Autowired
    private FiliereService filiereService;

    @GetMapping
    public String getAllCours(Model model) {
        model.addAttribute("cours", coursService.getAllCours());
        return "cours/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("filieres", filiereService.getAllFilieres());
        return "cours/add";
    }

    @PostMapping("/add")
    public String addCours(@ModelAttribute Cours cours,
                           @RequestParam(value = "filiereId", required = false) Long filiereId) {
        Cours savedCours = coursService.saveCours(cours);

        if (filiereId != null) {
            coursService.assignerFiliere(savedCours.getId(), filiereId);
        }
        return "redirect:/cours";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Cours cours = coursService.getCoursById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        model.addAttribute("cours", cours);
        model.addAttribute("filieres", filiereService.getAllFilieres());
        return "cours/edit";
    }

    @PostMapping("/update/{id}")
    public String updateCours(@PathVariable Long id,
                              @ModelAttribute Cours cours,
                              @RequestParam(value = "filiereId", required = false) Long filiereId) {
        coursService.updateCours(id, cours);

        if (filiereId != null) {
            coursService.assignerFiliere(id, filiereId);
        } else {
            Cours existingCours = coursService.getCoursById(id)
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
            existingCours.setFiliere(null);
            coursService.saveCours(existingCours);
        }
        return "redirect:/cours";
    }

    @GetMapping("/delete/{id}")
    public String deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return "redirect:/cours";
    }

    @GetMapping("/filiere/{filiereId}")
    public String getCoursByFiliere(@PathVariable Long filiereId, Model model) {
        model.addAttribute("cours", coursService.getCoursByFiliere(filiereId));
        model.addAttribute("filiere", filiereService.getFiliereById(filiereId).orElse(null));
        return "cours/list";
    }

    @GetMapping("/search")
    public String searchCours(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("cours", coursService.searchCours(keyword));
        model.addAttribute("keyword", keyword);
        return "cours/list";
    }
}