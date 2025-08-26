package app.gestion.GRH.controller;

import app.gestion.GRH.model.RubriquePaie;
import app.gestion.GRH.service.RubriquePaieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/rubrique")
@RequiredArgsConstructor
public class RubriquePaieController {
    private final RubriquePaieService rubriquePaieService;

    @GetMapping
    public List<RubriquePaie> all() {
        return rubriquePaieService.getAll();
    }

    @PostMapping
    public RubriquePaie create(@RequestBody RubriquePaie rubrique) {
        return rubriquePaieService.create(rubrique);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RubriquePaie> get(@PathVariable String id) {
        return rubriquePaieService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RubriquePaie> update(@PathVariable String id, @RequestBody RubriquePaie newRubrique) {
        return rubriquePaieService.update(id, newRubrique)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        rubriquePaieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
