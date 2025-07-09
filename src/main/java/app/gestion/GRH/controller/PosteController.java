package app.gestion.GRH.controller;

import app.gestion.GRH.model.Poste;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.service.PosteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/poste")
@RequiredArgsConstructor
public class PosteController {
    private final PosteService posteService;

    @GetMapping
    public List<Poste> all() {
        return posteService.getAll();
    }

    @PostMapping
    public Poste create(@RequestBody Poste poste) {
        return posteService.create(poste);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poste> get(@PathVariable String id) {
        return posteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poste> update(@PathVariable String id, @RequestBody Poste newPoste) {
        return posteService.update(id, newPoste)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        posteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
