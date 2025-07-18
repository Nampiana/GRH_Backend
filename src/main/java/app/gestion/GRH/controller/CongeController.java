package app.gestion.GRH.controller;

import app.gestion.GRH.model.Conge;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.service.CongeService;
import app.gestion.GRH.service.IndividuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/conge")
@RequiredArgsConstructor
public class CongeController {
    private final CongeService congeService;

    @GetMapping
    public List<Conge> all() {
        return congeService.getAll();
    }

    @PostMapping
    public Conge create(@RequestBody Conge conge) {
        return congeService.create(conge);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conge> get(@PathVariable String id) {
        return congeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conge> update(@PathVariable String id, @RequestBody Conge newConge) {
        return congeService.update(id, newConge)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        congeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
