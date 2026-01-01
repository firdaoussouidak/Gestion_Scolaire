package org.example.projet_24_12.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eleves")
public class Eleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    @ManyToMany
    @JoinTable(
            name = "eleve_cours",
            joinColumns = @JoinColumn(name = "eleve_id"),
            inverseJoinColumns = @JoinColumn(name = "cours_id")
    )
    private List<Cours> coursSuivis = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dossier_id", referencedColumnName = "id")
    private DossierAdministratif dossierAdministratif;

    public Eleve() {
    }

    public Eleve(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Cours> getCoursSuivis() {
        return coursSuivis;
    }

    public void setCoursSuivis(List<Cours> coursSuivis) {
        this.coursSuivis = coursSuivis;
    }

    public DossierAdministratif getDossierAdministratif() {
        return dossierAdministratif;
    }

    public void setDossierAdministratif(DossierAdministratif dossierAdministratif) {
        this.dossierAdministratif = dossierAdministratif;
    }


    public void ajouterCours(Cours cours) {
        if (!coursSuivis.contains(cours)) {
            coursSuivis.add(cours);
        }
    }

    public void supprimerCours(Cours cours) {
        coursSuivis.remove(cours);
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}