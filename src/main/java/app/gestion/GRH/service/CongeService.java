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
            notifyRhOnNewLeave(saved);
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

    public Optional<Conge> update(String id, Conge newConge) {
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
}
