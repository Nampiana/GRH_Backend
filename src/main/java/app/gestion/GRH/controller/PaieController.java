// controller/PaieController.java
package app.gestion.GRH.controller;

import app.gestion.GRH.dto.BulletinPaieDTO;
import app.gestion.GRH.dto.LignePaieDTO;
import app.gestion.GRH.model.EmployerSociete;
import app.gestion.GRH.repository.EmployerSocieteRepository;
import app.gestion.GRH.service.PaieService;
import lombok.*;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/paie")
@RequiredArgsConstructor
public class PaieController {

    private final PaieService paieService;
    private final EmployerSocieteRepository employerRepo;

    @GetMapping("/calculer")
    public BulletinPaieDTO calculer(
            @RequestParam String idEmployer,
            @RequestParam String moisPaieId
    ) {
        return paieService.calculerBulletin(idEmployer, moisPaieId);
    }

    @PostMapping("/enregistrer")
    public void enregistrer(@RequestBody EnregistrerRequest req) {
        EmployerSociete emp = employerRepo.findById(req.getIdEmployer())
                .orElseThrow(() -> new RuntimeException("Employer introuvable"));
        paieService.enregistrerCalcul(req.getIdEmployer(), req.getMoisPaieId(), req.getLignes(), emp.getIdCategorie());
    }

    @Data
    public static class EnregistrerRequest {
        private String idEmployer;
        private String moisPaieId;
        private List<LignePaieDTO> lignes;
    }
}
