// controller/CyclePaieController.java
package app.gestion.GRH.controller;

import app.gestion.GRH.model.MoisPaie;
import app.gestion.GRH.service.CyclePaieService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cycle-paie")
@RequiredArgsConstructor
public class CyclePaieController {

    private final CyclePaieService cyclePaieService;

    @PostMapping("/ouvrir")
    public ResponseEntity<MoisPaie> ouvrir(@RequestBody OuvrirRequest req) {
        YearMonth ym = YearMonth.parse(req.getPeriode()); // "2025-08"
        MoisPaie m = cyclePaieService.ouvrirMois(req.getIdSociete(), ym);
        return ResponseEntity.ok(m);
    }

    @PostMapping("/cloturer/{moisPaieId}")
    public ResponseEntity<MoisPaie> cloturer(@PathVariable String moisPaieId) {
        MoisPaie m = cyclePaieService.cloturerMois(moisPaieId);
        return ResponseEntity.ok(m);
    }

    @Data
    public static class OuvrirRequest {
        private String idSociete;
        private String periode; // "YYYY-MM"
    }
}
