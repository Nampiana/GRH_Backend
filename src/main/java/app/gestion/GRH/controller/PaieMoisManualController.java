// controller/PaieMoisManualController.java
package app.gestion.GRH.controller;

import app.gestion.GRH.model.*;
import app.gestion.GRH.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/paieMois")
@RequiredArgsConstructor
public class PaieMoisManualController {

    private final EmployerSocieteRepository employerRepo;
    private final RubriquePaieRepository rubriqueRepo;
    private final RubriqueCategorieRepository rubCatRepo;
    private final PaieMoisRepository paieMoisRepo;

    @PostMapping("/upsert")
    public void upsert(@RequestBody UpsertRequest req) {
        EmployerSociete emp = employerRepo.findById(req.getIdEmployer())
                .orElseThrow(() -> new RuntimeException("Employer introuvable"));

        // map code -> rubrique
        Map<String, RubriquePaie> byCode = rubriqueRepo.findAll().stream()
                .collect(Collectors.toMap(r -> r.getCode().toUpperCase(Locale.ROOT), r -> r));

        // map rubriqueId -> rubCatId pour la catégorie de l’employé
        Map<String, String> rubIdToRubCatId = rubCatRepo.findByIdCategorie(emp.getIdCategorie()).stream()
                .collect(Collectors.toMap(RubriqueCategorie::getIdRubriquePaie, RubriqueCategorie::getId));

        // upsert uniquement les lignes envoyées (SB/PRIME/HS/AVANCE)
        List<PaieMois> existants = paieMoisRepo.findByIdEmployerAndMoisPaieId(req.getIdEmployer(), req.getMoisPaieId());

        for (LigneManuelle l : req.getLignes()) {
            RubriquePaie r = byCode.get(l.getCode().toUpperCase(Locale.ROOT));
            if (r == null) continue;

            // ❌ Bloquer toute écriture de SB : c'est le salaire du contrat
            if ("SB".equalsIgnoreCase(r.getCode())) continue;

            String rubCatId = rubIdToRubCatId.get(r.getId());
            if (rubCatId == null) continue;

            PaieMois cible = existants.stream()
                    .filter(p -> p.getIdRubriqueCat().equals(rubCatId))
                    .findFirst().orElse(null);

            if (cible == null) {
                cible = new PaieMois();
                cible.setIdEmployer(req.getIdEmployer());
                cible.setMoisPaieId(req.getMoisPaieId());
                cible.setIdRubriqueCat(rubCatId);
            }
            cible.setValeur(l.getMontant());
            cible.setNote(l.getNote());
            paieMoisRepo.save(cible);
        }
    }

    @Data
    public static class UpsertRequest {
        private String idEmployer;
        private String moisPaieId;
        private List<LigneManuelle> lignes;
    }
    @Data
    public static class LigneManuelle {
        private String code;     // "SB", "PRIME", "HS", "AVANCE"
        private Double montant;  // MGA
        private String note;     // optionnel
    }
}
