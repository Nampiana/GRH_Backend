package app.gestion.GRH.controller;

import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Poste;
import app.gestion.GRH.service.IndividuService;
import app.gestion.GRH.service.PosteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/individu")
@RequiredArgsConstructor
public class IndividuController {
    private final IndividuService individuService;

    @GetMapping
    public List<Individu> all() {
        return individuService.getAll();
    }

    @PostMapping
    public Individu create(@RequestBody Individu poste) {
        return individuService.create(poste);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Individu> get(@PathVariable String id) {
        return individuService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Individu> update(@PathVariable String id, @RequestBody Individu newIndividu) {
        return individuService.update(id, newIndividu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        individuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
