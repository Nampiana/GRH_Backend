package app.gestion.GRH.service;

import app.gestion.GRH.model.EmployerSociete;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Utilisateur;
import app.gestion.GRH.repository.EmployerSocieteRepository;
import app.gestion.GRH.repository.IndividuRepository;
import app.gestion.GRH.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SocieteService societeService;

  /*  public EmployerSociete createEmployerSociete(EmployerSociete employerSociete, Individu individuData, String idSociete, Integer role) {
        // Créer Individu
        Individu individu = Individu.builder()
                .nom(individuData.getNom())
                .prenom(individuData.getPrenom())
                .adresse(individuData.getAdresse())
                .email(individuData.getEmail())
                .password(passwordEncoder.encode(individuData.getPassword()))
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
        employerSociete.setIdUtilisateur(utilisateur.getId());
        employerSociete.setDateEmbauche(new Date());

        return employerSocieteRepository.save(employerSociete);
    }*/

    public EmployerSociete createEmployerSociete(EmployerSociete employerSociete, Individu individuData, String idSociete, Integer role) {
        // Créer Individu
        Individu individu = Individu.builder()
                .nom(individuData.getNom())
                .prenom(individuData.getPrenom())
                .adresse(individuData.getAdresse())
                .email(individuData.getEmail())
                .password(passwordEncoder.encode(individuData.getPassword()))
                .telephone(individuData.getTelephone())
                .build();

        individu = individuRepository.save(individu);

        // Créer Utilisateur
        Utilisateur utilisateur = Utilisateur.builder()
                .idIndividu(individu.getId())
                .idSociete(idSociete)
                .etat(1)
                .roles(role)
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);

        // Compléter EmployerSociete
        employerSociete.setIdIndividue(individu.getId());
        employerSociete.setIdUtilisateur(utilisateur.getId());
        employerSociete.setDateEmbauche(new Date());
        EmployerSociete saved = employerSocieteRepository.save(employerSociete);

        // Envoi d'email
     /*   emailService.sendCredentials(
                individu.getEmail(),
                individu.getNom(),
                individu.getPrenom(),
                individu.getEmail(),
                individuData.getPassword(), // Attention ici, on envoie le mot de passe en clair
                societeService.findById(employerSociete.getIdSociete()).get().getNomSociete()
        );*/

        return saved;
    }


    public List<EmployerSociete> getAll() {
        return employerSocieteRepository.findAll();
    }

    public Optional<EmployerSociete> getById(String id) {
        return employerSocieteRepository.findById(id);
    }

  /*  public Optional<EmployerSociete> update(String id, EmployerSociete updated) {
        return employerSocieteRepository.findById(id).map(e -> {
            e.setIdSociete(updated.getIdSociete());
            e.setIdService(updated.getIdService());
            e.setIdPoste(updated.getIdPoste());
            e.setIdCategorie(updated.getIdCategorie());
            e.setDateDebauche(updated.getDateDebauche());
            return employerSocieteRepository.save(e);
        });
    }*/

    public Optional<EmployerSociete> updateComplete(
            String id,
            EmployerSociete updatedEmployerSociete,
            Individu updatedIndividu
    ) {
        return employerSocieteRepository.findById(id).map(employer -> {
            // MAJ EmployerSociete
            employer.setIdSociete(updatedEmployerSociete.getIdSociete());
            employer.setIdService(updatedEmployerSociete.getIdService());
            employer.setIdPoste(updatedEmployerSociete.getIdPoste());
            employer.setIdCategorie(updatedEmployerSociete.getIdCategorie());
            employer.setDateDebauche(updatedEmployerSociete.getDateDebauche());
            employer.setSalaireBase(updatedEmployerSociete.getSalaireBase());
            employerSocieteRepository.save(employer);

            // MAJ Individu
            individuRepository.findById(employer.getIdIndividue()).ifPresent(ind -> {
                ind.setNom(updatedIndividu.getNom());
                ind.setPrenom(updatedIndividu.getPrenom());
                ind.setAdresse(updatedIndividu.getAdresse());
                ind.setEmail(updatedIndividu.getEmail());
                ind.setTelephone(updatedIndividu.getTelephone());
                if (updatedIndividu.getPassword() != null && !updatedIndividu.getPassword().isEmpty()) {
                    ind.setPassword(passwordEncoder.encode(updatedIndividu.getPassword()));
                }
                individuRepository.save(ind);
            });

            return employer;
        });
    }


   /* public void delete(String id) {
        employerSocieteRepository.deleteById(id);
    }*/

    public void deleteComplete(String id) {
        employerSocieteRepository.findById(id).ifPresent(employer -> {
            String idIndividu = employer.getIdIndividue();

            // Suppression de l'individu
            individuRepository.findById(idIndividu)
                    .ifPresent(individuRepository::delete);

            // Suppression de l'employer societe
            employerSocieteRepository.deleteById(id);
        });
    }

    public Optional<EmployerSociete> getByIdUtilisateur(String idUtilisateur) {
        return employerSocieteRepository.findByIdUtilisateur(idUtilisateur);
    }

}
