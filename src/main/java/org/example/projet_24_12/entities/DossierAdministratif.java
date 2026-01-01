package org.example.projet_24_12.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "dossiers_administratifs")
public class DossierAdministratif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroInscription;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @OneToOne(mappedBy = "dossierAdministratif")
    private Eleve eleve;

    public DossierAdministratif() {
        this.dateCreation = LocalDate.now();
    }

    public DossierAdministratif(String numeroInscription) {
        this();
        this.numeroInscription = numeroInscription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroInscription() {
        return numeroInscription;
    }

    public void setNumeroInscription(String numeroInscription) {
        this.numeroInscription = numeroInscription;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public String getDateCreationFormatted() {
        if (dateCreation != null) {
            return dateCreation.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }
}