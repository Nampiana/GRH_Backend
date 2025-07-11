package app.gestion.GRH.service;

import app.gestion.GRH.model.EmployerSociete;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Utilisateur;
import app.gestion.GRH.repository.EmployerSocieteRepository;
import app.gestion.GRH.repository.IndividuRepository;
import app.gestion.GRH.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerSocieteService {
    private final EmployerSocieteRepository employerSocieteRepository;
    private final IndividuRepository individuRepository;
    private final UtilisateurRepository utilisateurRepository;

    public EmployerSociete createEmployerSociete(EmployerSociete employerSociete, Individu individuData, String idSociete, Integer role) {
        // Créer Individu
        Individu individu = Individu.builder()
                .nom(individuData.getNom())
                .prenom(individuData.getPrenom())
                .adresse(individuData.getAdresse())
                .email(individuData.getEmail())
                .password(individuData.getPassword())
                .telephone(individuData.getTelephone())
                .build();

        individu = individuRepository.save(individu);

        // Créer Utilisateur
        Utilisateur utilisateur = Utilisateur.builder()
                .idIndividu(individu.getId())
                .idSociete(idSociete)
                .etat(1) // compte activé par défaut
                .roles(role) // rôle employé par défaut
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);

        // Compléter EmployerSociete
        employerSociete.setIdIndividue(individu.getId());
        employerSociete.setDateEmbauche(new Date());

        return employerSocieteRepository.save(employerSociete);
    }

    public List<EmployerSociete> getAll() {
        return employerSocieteRepository.findAll();
    }

    public Optional<EmployerSociete> getById(String id) {
        return employerSocieteRepository.findById(id);
    }

    public Optional<EmployerSociete> update(String id, EmployerSociete updated) {
        return employerSocieteRepository.findById(id).map(e -> {
            e.setIdSociete(updated.getIdSociete());
            e.setIdService(updated.getIdService());
            e.setIdPoste(updated.getIdPoste());
            e.setIdCategorie(updated.getIdCategorie());
            e.setDateDebauche(updated.getDateDebauche());
            return employerSocieteRepository.save(e);
        });
    }

    public void delete(String id) {
        employerSocieteRepository.deleteById(id);
    }
}
