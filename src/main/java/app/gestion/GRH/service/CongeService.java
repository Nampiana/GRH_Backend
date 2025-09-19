package app.gestion.GRH.service;

import app.gestion.GRH.model.Conge;
import app.gestion.GRH.model.EmployerSociete;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Utilisateur;
import app.gestion.GRH.repository.CongeRepository;
import app.gestion.GRH.repository.EmployerSocieteRepository;
import app.gestion.GRH.repository.IndividuRepository;
import app.gestion.GRH.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CongeService {
    private final CongeRepository congeRepository;

    // 🔗 NOUVEAU : pour récupérer société / RH / emails
    private final EmployerSocieteRepository employerSocieteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final IndividuRepository individuRepository;
    private final EmailService emailService;
    private final SocieteService societeService; // si tu veux le nom de la société

    public List<Conge> getAll(){
        return congeRepository.findAll();
    }

    public Conge create(Conge conge){
        Conge saved = congeRepository.save(conge);

        try {
            System.out.printf("Nouveau congé créé avec ID, employé, date début et date fin%n");

            // notifyRhOnNewLeave(saved);

        } catch (Exception e) {
            // on log seulement, on ne casse pas la création si email échoue
            e.printStackTrace();
        }

        return saved;
    }

    public Optional<Conge> findById(String id){
        return congeRepository.findById(id);
    }

    public void delete(String id){
        congeRepository.deleteById(id);
    }

 /*   public Optional<Conge> update(String id, Conge newConge) {
        return congeRepository.findById(id).map(c -> {
            c.setIdEmployerSociete(newConge.getIdEmployerSociete());
            c.setDateDebut(newConge.getDateDebut());
            c.setDateFin(newConge.getDateFin());
            c.setMotif(newConge.getMotif());
            c.setStatut(newConge.getStatut());
            c.setDuree(newConge.getDuree());
            c.setCommentaire(newConge.getCommentaire());
            c.setDateCreation(newConge.getDateCreation());
            return congeRepository.save(c);
        });
    }*/

    public Optional<Conge> update(String id, Conge newConge) {
        return congeRepository.findById(id).map(old -> {
            Integer oldStatut = old.getStatut();

            old.setIdEmployerSociete(newConge.getIdEmployerSociete());
            old.setDateDebut(newConge.getDateDebut());
            old.setDateFin(newConge.getDateFin());
            old.setMotif(newConge.getMotif());
            old.setStatut(newConge.getStatut());
            old.setDuree(newConge.getDuree());
            old.setCommentaire(newConge.getCommentaire());
            old.setDateCreation(newConge.getDateCreation());

            Conge saved = congeRepository.save(old);

            // ✅ si le statut passe de 1 (ou autre) vers 2/3 → email au salarié
            try {
              /*  if (saved.getStatut() != null
                        && (saved.getStatut() == 2 || saved.getStatut() == 3)
                        && !Objects.equals(oldStatut, saved.getStatut())) {
                    notifyEmployeeOnDecision(saved);
                }*/
                System.out.printf("Nouveau congé créé avec ID, employé, date début et date fin%n");

            } catch (Exception e) {
                e.printStackTrace(); // on ne casse pas la MAJ si l’email échoue
            }

            return saved;
        });
    }

    // =======================
    // PRIVÉS
    // =======================
    private void notifyRhOnNewLeave(Conge conge) {
        if (conge == null || conge.getIdEmployerSociete() == null) return;

        // 1) Récupérer l'employeur-société pour connaître la société + individu (employé)
        Optional<EmployerSociete> empOpt = employerSocieteRepository.findById(conge.getIdEmployerSociete());
        if (empOpt.isEmpty()) return;

        EmployerSociete emp = empOpt.get();
        String idSociete = emp.getIdSociete();
        String idIndividuEmploye = emp.getIdIndividue();

        // 2) Tous les RH actifs de la société
        List<Utilisateur> rhs = utilisateurRepository.findByIdSocieteAndRolesAndEtat(idSociete, 2, 1);

        if (rhs.isEmpty()) return;

        // 3) Convertir en emails (via Individu)
        Map<String, Individu> individuById = individuRepository
                .findAllById(
                        rhs.stream().map(Utilisateur::getIdIndividu).filter(Objects::nonNull).collect(Collectors.toSet())
                )
                .stream().collect(Collectors.toMap(Individu::getId, it -> it));

        List<String> toEmails = rhs.stream()
                .map(Utilisateur::getIdIndividu)
                .map(individuById::get)
                .filter(Objects::nonNull)
                .map(Individu::getEmail)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .distinct()
                .collect(Collectors.toList());

        if (toEmails.isEmpty()) return;

        // 4) Info employé (nom/email) pour le corps du mail
        String employeNomComplet = "Employé inconnu";
        String employeEmail = "—";
        if (idIndividuEmploye != null) {
            individuRepository.findById(idIndividuEmploye).ifPresent(ind -> {
                // closure trick: use array or build below
            });
            Optional<Individu> empIndOpt = individuRepository.findById(idIndividuEmploye);
            if (empIndOpt.isPresent()) {
                Individu ind = empIndOpt.get();
                employeNomComplet = ((ind.getPrenom() != null ? ind.getPrenom() : "") + " " + (ind.getNom() != null ? ind.getNom() : "")).trim();
                if (employeNomComplet.isBlank()) employeNomComplet = "Employé";
                employeEmail = ind.getEmail() != null ? ind.getEmail() : "—";
            }
        }

        // 5) Nom société (si service dispo)
        String nomSociete = "Votre société";
        try {
            nomSociete = societeService.findById(idSociete).map(s -> {
                // adapte le champ selon ton modèle (nomSociete / raisonSociale / name)
                try {
                    return (String) s.getClass().getMethod("getNomSociete").invoke(s);
                } catch (Exception e) {
                    return "Votre société";
                }
            }).orElse("Votre société");
        } catch (Exception ignored) {}

        // 6) Formatage des dates / durée
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dDeb = conge.getDateDebut() != null ? df.format(conge.getDateDebut()) : "—";
        String dFin = conge.getDateFin() != null ? df.format(conge.getDateFin()) : "—";
        String dureeTxt = conge.getDuree() != null ? (conge.getDuree() + " jour(s)") : "—";
        String motif = conge.getMotif();
        String commentaire = conge.getCommentaire();

        // 7) Envoi
        emailService.sendLeaveRequest(
                toEmails.toArray(new String[0]),
                nomSociete,
                employeNomComplet,
                employeEmail,
                dDeb,
                dFin,
                dureeTxt,
                motif,
                commentaire
        );
    }

    private void notifyEmployeeOnDecision(Conge conge) {
        if (conge == null || conge.getIdEmployerSociete() == null) return;

        // 1) Récupérer le lien employeur-société
        Optional<EmployerSociete> empOpt = employerSocieteRepository.findById(conge.getIdEmployerSociete());
        if (empOpt.isEmpty()) return;

        EmployerSociete emp = empOpt.get();
        String idSociete = emp.getIdSociete();
        String idIndividuEmploye = emp.getIdIndividue();

        // 2) Récupérer l'individu (salarié) pour email + nom complet
        String employeEmail = null;
        String employeNomComplet = "Employé";
        if (idIndividuEmploye != null) {
            Optional<Individu> indOpt = individuRepository.findById(idIndividuEmploye);
            if (indOpt.isPresent()) {
                Individu ind = indOpt.get();

                String nom = (ind.getNom() == null ? "" : ind.getNom());
                String prenom = (ind.getPrenom() == null ? "" : ind.getPrenom());
                String full = (prenom + " " + nom).trim();
                if (!full.isEmpty()) {
                    employeNomComplet = full;
                }

                employeEmail = ind.getEmail();
            }
        }
        if (employeEmail == null || employeEmail.isBlank()) return;

        // 3) Nom de la société (fallback si le modèle diffère)
        String nomSociete = "Votre société";
        try {
            nomSociete = societeService.findById(idSociete).map(s -> {
                try {
                    return (String) s.getClass().getMethod("getNomSociete").invoke(s);
                } catch (Exception e) {
                    return "Votre société";
                }
            }).orElse("Votre société");
        } catch (Exception ignored) {}

        // 4) Formatage des champs (dates / durée)
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dDeb = (conge.getDateDebut() != null) ? df.format(conge.getDateDebut()) : "—";
        String dFin = (conge.getDateFin() != null) ? df.format(conge.getDateFin()) : "—";
        String dureeTxt = (conge.getDuree() != null) ? (conge.getDuree() + " jour(s)") : "—";
        String motif = conge.getMotif();
        String commentaireRh = conge.getCommentaire();

        // 5) Envoi
        emailService.sendLeaveDecision(
                employeEmail,
                nomSociete,
                employeNomComplet,
                dDeb,
                dFin,
                dureeTxt,
                motif,
                commentaireRh,
                conge.getStatut()
        );
    }
}
