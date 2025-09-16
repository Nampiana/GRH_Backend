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
    private final ParametreGenreauxRepository paramRepo;

    private double ar(double v) { return Math.rint(v); } // arrondi MGA

    public BulletinPaieDTO calculerBulletin(String idEmployer, String moisPaieId) {
        EmployerSociete emp = employerRepo.findById(idEmployer)
                .orElseThrow(() -> new RuntimeException("Employer introuvable"));

        // ❌  Bloquer le calcul si l’employé est déjà débouché (peu importe la date)
        if (emp.getDateDebauche() != null) {
            throw new IllegalStateException("Paie non calculable : l’employé est déjà débouché.");
        }

        MoisPaie mois = moisPaieRepo.findById(moisPaieId)
                .orElseThrow(() -> new RuntimeException("MoisPaie introuvable"));

        // ✅ Salaire de base depuis EmployerSociete
        double salaireBase = Optional.ofNullable(emp.getSalaireBase())
                .orElseThrow(() -> new RuntimeException("Salaire de base non défini sur l'employé"));

        // Rubriques applicables à la catégorie de l'employé
        List<RubriqueCategorie> rubCats = rubriqueCatRepo.findByIdCategorie(emp.getIdCategorie());
        Map<String, RubriquePaie> rubriquesById = rubCats.stream()
                .map(rc -> rubriqueRepo.findById(rc.getIdRubriquePaie())
                        .orElseThrow(() -> new RuntimeException("Rubrique introuvable: " + rc.getIdRubriquePaie())))
                .collect(Collectors.toMap(RubriquePaie::getId, r -> r));

        // Saisies du mois (hors SB désormais)
        List<PaieMois> saisies = paieMoisRepo.findByIdEmployerAndMoisPaieId(idEmployer, moisPaieId);

        // ---------- Trouver la rubrique SB (affichage + cohérence) ----------
        RubriquePaie sbRub = rubriqueRepo.findByCode("SB")
                .orElseThrow(() -> new RuntimeException("Rubrique 'SB' non configurée"));
        String sbRubId = sbRub.getId();

        // Id RubriqueCategorie lié à SB pour cette catégorie (utile si tu gardes la table de mapping)
        String sbRubCatId = rubCats.stream()
                .filter(rc -> rc.getIdRubriquePaie().equals(sbRubId))
                .map(RubriqueCategorie::getId)
                .findFirst()
                .orElse(null); // peut être null si tu ne relies pas SB aux catégories

        List<LignePaieDTO> lignes = new ArrayList<>();

        // ---------- Agrégats ----------
        double plusImposable = 0.0;     // + avec type = I
        double plusNonImposable = 0.0;  // + avec type = N
        double cotisations = 0.0;       // tous les -
        double irsaAmount = 0.0;

        // ---------- 1) SB (depuis EmployerSociete.salaireBase) ----------
        lignes.add(new LignePaieDTO("SB", sbRub.getNomRubrique(), 1, ar(salaireBase), null));
        // SB est imposable par convention
        plusImposable += salaireBase;

        // ---------- 2) Lignes manuelles (sans param généraux), en ignorant toute saisie "SB" ----------
        for (PaieMois p : saisies) {
            if (sbRubCatId != null && p.getIdRubriqueCat().equals(sbRubCatId)) {
                // On ignore toute tentative de saisir SB manuellement
                continue;
            }

            RubriqueCategorie rc = rubCats.stream()
                    .filter(r -> r.getId().equals(p.getIdRubriqueCat()))
                    .findFirst().orElse(null);
            if (rc == null) continue;

            RubriquePaie r = rubriquesById.get(rc.getIdRubriquePaie());
            if (r == null) continue;

            // ignorer ici les rubriques paramétrées (calculées ensuite)
            if (r.getIdParametreGenereaux() != null) continue;

            lignes.add(new LignePaieDTO(r.getCode(), r.getNomRubrique(), r.getOperation(), ar(p.getValeur()), null));

            if (r.getOperation() != null && r.getOperation() == 1) {
                if ("I".equalsIgnoreCase(r.getTypeRubrique())) {
                    plusImposable += p.getValeur();
                } else if ("N".equalsIgnoreCase(r.getTypeRubrique())) {
                    plusNonImposable += p.getValeur();
                }
            } else {
                cotisations += p.getValeur();
            }
        }

        // ---------- 3) Rubriques paramétrées ----------
        double brutImposable = ar(plusImposable);

        for (RubriquePaie r : rubriquesById.values()) {
            if (r.getIdParametreGenereaux() == null) continue;
            if ("SB".equalsIgnoreCase(r.getCode())) continue; // pas de % sur SB

            ParametreGenreaux param = paramRepo.findById(r.getIdParametreGenereaux())
                    .orElseThrow(() -> new RuntimeException("Paramètre généraux introuvable: " + r.getIdParametreGenereaux()));
            Double taux = param.getPourcentage();
            if (taux == null) continue;

            // Base de calcul :
            // - IRSA: % du BRUT IMPOSABLE
            // - autres (CNAPS, OSTIE…): % du SB (EmployerSociete.salaireBase)
            double base = "IRSA".equalsIgnoreCase(r.getCode()) ? brutImposable : salaireBase;
            double montant = ar(base * (taux / 100.0));

            lignes.add(new LignePaieDTO(r.getCode(), r.getNomRubrique(), r.getOperation(), montant, taux));

            if (r.getOperation() != null && r.getOperation() == 1) {
                if ("I".equalsIgnoreCase(r.getTypeRubrique())) plusImposable += montant;
                else if ("N".equalsIgnoreCase(r.getTypeRubrique())) plusNonImposable += montant;
            } else {
                cotisations += montant;
                if ("IRSA".equalsIgnoreCase(r.getCode())) irsaAmount = montant;
            }
        }

        brutImposable = ar(plusImposable);

        // ---------- 4) Totaux finaux ----------
        double totalPlus  = lignes.stream().filter(l -> l.getOperation() == 1).mapToDouble(LignePaieDTO::getMontant).sum();
        double totalMoins = lignes.stream().filter(l -> l.getOperation() == 0).mapToDouble(LignePaieDTO::getMontant).sum();
        double brut = ar(totalPlus);
        double net  = ar(brut - totalMoins);

        return BulletinPaieDTO.builder()
                .idEmployer(idEmployer)
                .moisPaieId(moisPaieId)
                .lignes(lignes)
                .totalPlus(ar(totalPlus))
                .totalMoins(ar(totalMoins))
                .brut(brut)
                .netAPayer(net)
                .plusImposable(ar(plusImposable))
                .plusNonImposable(ar(plusNonImposable))
                .cotisations(ar(cotisations))
                .brutImposable(brutImposable)
                .irsa(irsaAmount > 0 ? ar(irsaAmount) : null)
                .build();
    }

    // --- inchangé pour l’instant ---
    public void enregistrerCalcul(String idEmployer, String moisPaieId, List<LignePaieDTO> lignes, String idCategorie) {
        paieMoisRepo.deleteByIdEmployerAndMoisPaieId(idEmployer, moisPaieId);

        Map<String, RubriquePaie> byCode = rubriqueRepo.findAll().stream()
                .collect(Collectors.toMap(RubriquePaie::getCode, r -> r));

        Map<String, String> rubIdToRubCatId = rubriqueCatRepo.findByIdCategorie(idCategorie).stream()
                .collect(Collectors.toMap(RubriqueCategorie::getIdRubriquePaie, RubriqueCategorie::getId));

        for (LignePaieDTO l : lignes) {
            RubriquePaie r = byCode.get(l.getCode());
            if (r == null) continue;

            // ❌ Ne jamais enregistrer SB depuis le front (source = EmployerSociete)
            if ("SB".equalsIgnoreCase(r.getCode())) continue;

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

