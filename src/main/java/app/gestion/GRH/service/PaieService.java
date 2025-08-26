// service/PaieService.java
package app.gestion.GRH.service;

import app.gestion.GRH.dto.BulletinPaieDTO;
import app.gestion.GRH.dto.LignePaieDTO;
import app.gestion.GRH.model.*;
import app.gestion.GRH.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaieService {

    private final EmployerSocieteRepository employerRepo;
    private final RubriqueCategorieRepository rubriqueCatRepo;
    private final RubriquePaieRepository rubriqueRepo;
    private final PaieMoisRepository paieMoisRepo;
    private final MoisPaieRepository moisPaieRepo;

    // ✅ nouveau: pour récupérer le pourcentage via idParametreGenereaux
    private final ParametreGenreauxRepository paramRepo;

    private double ar(double v) { return Math.rint(v); } // arrondi MGA

    public BulletinPaieDTO calculerBulletin(String idEmployer, String moisPaieId) {
        EmployerSociete emp = employerRepo.findById(idEmployer)
                .orElseThrow(() -> new RuntimeException("Employer introuvable"));

        MoisPaie mois = moisPaieRepo.findById(moisPaieId)
                .orElseThrow(() -> new RuntimeException("MoisPaie introuvable"));

        List<RubriqueCategorie> rubCats = rubriqueCatRepo.findByIdCategorie(emp.getIdCategorie());
        Map<String, RubriquePaie> rubriquesById = rubCats.stream()
                .map(rc -> rubriqueRepo.findById(rc.getIdRubriquePaie())
                        .orElseThrow(() -> new RuntimeException("Rubrique introuvable: " + rc.getIdRubriquePaie())))
                .collect(Collectors.toMap(RubriquePaie::getId, r -> r));

        List<PaieMois> saisies = paieMoisRepo.findByIdEmployerAndMoisPaieId(idEmployer, moisPaieId);

        // Salaire de base (SB)
        RubriquePaie sbRub = rubriqueRepo.findByCode("SB")
                .orElseThrow(() -> new RuntimeException("Rubrique 'SB' non configurée"));
        String sbRubId = sbRub.getId();
        String sbRubCatId = rubCats.stream()
                .filter(rc -> rc.getIdRubriquePaie().equals(sbRubId))
                .map(RubriqueCategorie::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("La rubrique SB n'est pas liée à la catégorie de l'employé"));

        double salaireBase = saisies.stream()
                .filter(p -> p.getIdRubriqueCat().equals(sbRubCatId))
                .map(PaieMois::getValeur)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Salaire de base (SB) non saisi pour ce mois"));

        List<LignePaieDTO> lignes = new ArrayList<>();

        // 1) SB
        lignes.add(new LignePaieDTO("SB", sbRub.getNomRubrique(), 1, ar(salaireBase), null));

        // 2) Lignes manuelles (aucun paramètre généraux associé)
        for (PaieMois p : saisies) {
            if (p.getIdRubriqueCat().equals(sbRubCatId)) continue;
            RubriqueCategorie rc = rubCats.stream().filter(r -> r.getId().equals(p.getIdRubriqueCat())).findFirst().orElse(null);
            if (rc == null) continue;

            RubriquePaie r = rubriquesById.get(rc.getIdRubriquePaie());
            if (r == null) continue;

            // ❗ ignorer ici si la rubrique est calculée via ParametreGenereaux
            if (r.getIdParametreGenereaux() != null) continue;

            lignes.add(new LignePaieDTO(r.getCode(), r.getNomRubrique(), r.getOperation(), ar(p.getValeur()), null));
        }

        // 3) Rubriques calculées via ParametreGenereaux (% du SB)
        for (RubriquePaie r : rubriquesById.values()) {
            if (r.getIdParametreGenereaux() == null) continue;
            if ("SB".equalsIgnoreCase(r.getCode())) continue;

            ParametreGenreaux param = paramRepo.findById(r.getIdParametreGenereaux())
                    .orElseThrow(() -> new RuntimeException("Paramètre généraux introuvable: " + r.getIdParametreGenereaux()));
            Double taux = param.getPourcentage(); // ex: 5.0
            if (taux == null) continue;

            double montant = ar(salaireBase * (taux / 100.0));
            lignes.add(new LignePaieDTO(r.getCode(), r.getNomRubrique(), r.getOperation(), montant, taux));
        }

        double totalPlus  = lignes.stream().filter(l -> l.getOperation() == 1).mapToDouble(LignePaieDTO::getMontant).sum();
        double totalMoins = lignes.stream().filter(l -> l.getOperation() == 0).mapToDouble(LignePaieDTO::getMontant).sum();
        double brut = totalPlus;
        double net  = ar(brut - totalMoins);

        return new BulletinPaieDTO(idEmployer, moisPaieId, lignes, ar(totalPlus), ar(totalMoins), ar(brut), net);
    }

    public void enregistrerCalcul(String idEmployer, String moisPaieId, List<LignePaieDTO> lignes, String idCategorie) {
        paieMoisRepo.deleteByIdEmployerAndMoisPaieId(idEmployer, moisPaieId);

        Map<String, RubriquePaie> byCode = rubriqueRepo.findAll().stream()
                .collect(Collectors.toMap(RubriquePaie::getCode, r -> r));

        Map<String, String> rubIdToRubCatId = rubriqueCatRepo.findByIdCategorie(idCategorie).stream()
                .collect(Collectors.toMap(RubriqueCategorie::getIdRubriquePaie, RubriqueCategorie::getId));

        for (LignePaieDTO l : lignes) {
            RubriquePaie r = byCode.get(l.getCode());
            if (r == null) continue;
            String rubCatId = rubIdToRubCatId.get(r.getId());
            if (rubCatId == null) continue;

            PaieMois p = new PaieMois();
            p.setIdEmployer(idEmployer);
            p.setMoisPaieId(moisPaieId);
            p.setIdRubriqueCat(rubCatId);
            p.setValeur(l.getMontant());
            p.setNote(l.getTaux() != null ? ("taux " + l.getTaux() + "%") : null);

            paieMoisRepo.save(p);
        }
    }
}
