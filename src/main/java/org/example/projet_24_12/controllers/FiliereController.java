package org.example.projet_24_12.controllers;

import org.example.projet_24_12.entities.Filiere;
import org.example.projet_24_12.services.FiliereService;
import org.example.projet_24_12.services.EleveService;
import org.example.projet_24_12.services.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/filieres")
public class FiliereController {

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private EleveService eleveService;

    @Autowired
    private CoursService coursService;

    @GetMapping
    public String getAllFilieres(Model model) {
        model.addAttribute("filieres", filiereService.getAllFilieres());
        return "filieres/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("filiere", new Filiere());
        return "filieres/add";
    }

    @PostMapping("/add")
    public String addFiliere(@ModelAttribute Filiere filiere) {
        if (filiereService.existsByCode(filiere.getCode())) {
            throw new RuntimeException("Une filière avec ce code existe déjà");
        }
        filiereService.saveFiliere(filiere);
        return "redirect:/filieres";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Filiere filiere = filiereService.getFiliereById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        model.addAttribute("filiere", filiere);
        return "filieres/edit";
    }

    @PostMapping("/update/{id}")
    public String updateFiliere(@PathVariable Long id, @ModelAttribute Filiere filiere) {
        filiereService.updateFiliere(id, filiere);
        return "redirect:/filieres";
    }

    @GetMapping("/delete/{id}")
    public String deleteFiliere(@PathVariable Long id) {
        filiereService.deleteFiliere(id);
        return "redirect:/filieres";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        Filiere filiere = filiereService.getFiliereById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        model.addAttribute("filiere", filiere);
        model.addAttribute("eleves", eleveService.getElevesByFiliere(id));
        model.addAttribute("cours", coursService.getCoursByFiliere(id));

        return "filieres/details";
    }

    @GetMapping("/eleves/{id}")
    public String showElevesByFiliere(@PathVariable Long id, Model model) {
        Filiere filiere = filiereService.getFiliereById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        model.addAttribute("filiere", filiere);
        model.addAttribute("eleves", eleveService.getElevesByFiliere(id));
        return "eleves/list";
    }

    @GetMapping("/cours/{id}")
    public String showCoursByFiliere(@PathVariable Long id, Model model) {
        Filiere filiere = filiereService.getFiliereById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

        model.addAttribute("filiere", filiere);
        model.addAttribute("cours", coursService.getCoursByFiliere(id));
        return "cours/list";
    }
}