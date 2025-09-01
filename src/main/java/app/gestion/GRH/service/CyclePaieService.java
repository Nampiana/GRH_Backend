// service/CyclePaieService.java
package app.gestion.GRH.service;

import app.gestion.GRH.model.*;
import app.gestion.GRH.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CyclePaieService {

    private final MoisPaieRepository moisPaieRepo;
    private final EmployerSocieteRepository employerRepo;
    private final SoldeCongeRepository soldeCongeRepo;
    private final RubriqueCategorieRepository rubCatRepo;
    private final RubriquePaieRepository rubriqueRepo;
    private final ParametreGenreauxRepository paramRepo;
    private final PaieMoisRepository paieMoisRepo;

    private double ar(double v) { return Math.rint(v); }

    /**
     * Ouvre un mois pour une société :
     * - crée MoisPaie(idSociete, periode, statut=OPEN)
     * - +2.5 jours à chaque employé actif
     * - génère lignes de paie par défaut : SB + rubriques paramétrées (%)
     * - NE touche pas aux rubriques manuelles (PRIME, AVANCE, HS) déjà saisies
     */
    @Transactional
    public MoisPaie ouvrirMois(String idSociete, YearMonth periode) {
        MoisPaie mois = moisPaieRepo.findByIdSocieteAndPeriode(idSociete, periode).orElseGet(() -> {
            MoisPaie m = new MoisPaie();
            m.setIdSociete(idSociete);
            m.setPeriode(periode);
            m.setStatut("OPEN");
            m.setDateOuverture(new Date());
            return moisPaieRepo.save(m);
        });

        // 1) +2.5 jours de solde
        List<EmployerSociete> actifs = employerRepo.findByIdSocieteAndDateDebaucheIsNull(idSociete);
        for (EmployerSociete emp : actifs) {
            SoldeConge sc = soldeCongeRepo.findByIdEmployerSociete(emp.getId());
            if (sc == null) {
                sc = new SoldeConge(null, emp.getId(), 0.0);
            }
            sc.setSolde((sc.getSolde() == null ? 0.0 : sc.getSolde()) + 2.5);
            soldeCongeRepo.save(sc);
        }

        // 2) Génération paie par défaut : SB + rubriques à % (CNAPS/OSTIE/IRSA…)
        //    On upsert SB et %; on ne supprime pas les lignes manuelles déjà présentes.
        Map<String, RubriquePaie> rubriquesById = rubriqueRepo.findAll().stream()
                .filter(r -> idSociete.equals(r.getIdSociete()))
                .collect(Collectors.toMap(RubriquePaie::getId, r -> r));
        Optional<RubriquePaie> sbOpt = rubriqueRepo.findByCode("SB");
        if (sbOpt.isEmpty())
            throw new RuntimeException("Rubrique 'SB' non configurée");
        RubriquePaie sbRub = sbOpt.get();

        for (EmployerSociete emp : actifs) {
            // a) Trouver rubriques liées à sa catégorie
            List<RubriqueCategorie> rubCats = rubCatRepo.findByIdCategorie(emp.getIdCategorie());
            Map<String, String> rubIdToRubCatId = rubCats.stream()
                    .collect(Collectors.toMap(RubriqueCategorie::getIdRubriquePaie, RubriqueCategorie::getId));

            // b) Upsert SB à salaireBase
            String sbRubCatId = rubIdToRubCatId.get(sbRub.getId());
            if (sbRubCatId == null)
                throw new RuntimeException("La rubrique SB n'est pas liée à la catégorie de l'employé: " + emp.getId());
            upsertPaie(emp.getId(), mois.getId(), sbRubCatId, ar(emp.getSalaireBase() == null ? 0.0 : emp.getSalaireBase()), "Salaire de base");

            // c) Rubriques % (CNAPS/OSTIE/etc.) → calculées sur SB
            for (RubriqueCategorie rc : rubCats) {
                RubriquePaie r = rubriquesById.get(rc.getIdRubriquePaie());
                if (r == null) continue;
                if (r.getIdParametreGenereaux() == null) continue; // manuel -> on n'y touche pas ici
                if ("SB".equalsIgnoreCase(r.getCode())) continue;

                double base = emp.getSalaireBase() == null ? 0.0 : emp.getSalaireBase();
                ParametreGenreaux param = paramRepo.findById(r.getIdParametreGenereaux())
                        .orElseThrow(() -> new RuntimeException("Paramètre généraux introuvable: " + r.getIdParametreGenereaux()));
                Double taux = param.getPourcentage();
                if (taux == null) continue;

                double montant = ar(base * (taux / 100.0));
                upsertPaie(emp.getId(), mois.getId(), rc.getId(), montant, "taux " + taux + "%");
            }
        }

        return mois;
    }

    /**
     * Clôture un mois (verrouille).
     */
    @Transactional
    public MoisPaie cloturerMois(String moisPaieId) {
        MoisPaie mois = moisPaieRepo.findById(moisPaieId)
                .orElseThrow(() -> new RuntimeException("MoisPaie introuvable"));
        mois.setStatut("CLOSED");
        mois.setDateCloture(new Date());
        return moisPaieRepo.save(mois);
    }

    private void upsertPaie(String idEmployer, String moisPaieId, String idRubriqueCat, double valeur, String note) {
        List<PaieMois> exist = paieMoisRepo.findByIdEmployerAndMoisPaieId(idEmployer, moisPaieId);
        PaieMois cible = exist.stream()
                .filter(p -> p.getIdRubriqueCat().equals(idRubriqueCat))
                .findFirst().orElse(null);

        if (cible == null) {
            cible = new PaieMois();
            cible.setIdEmployer(idEmployer);
            cible.setMoisPaieId(moisPaieId);
            cible.setIdRubriqueCat(idRubriqueCat);
        }
        cible.setValeur(valeur);
        cible.setNote(note);
        paieMoisRepo.save(cible);
    }
}
