package app.gestion.GRH.controller;

import app.gestion.GRH.model.RubriqueCategorie;
import app.gestion.GRH.service.RubriqueCategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/rubrique-categorie")
@RequiredArgsConstructor
public class RubriqueCategorieController {
    private final RubriqueCategorieService rubriqueCategorieService;

    @GetMapping
    public List<RubriqueCategorie> all() {
        return rubriqueCategorieService.getAll();
    }

    @PostMapping
    public RubriqueCategorie create(@RequestBody RubriqueCategorie rubriqueCategorie) {
        return rubriqueCategorieService.create(rubriqueCategorie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RubriqueCategorie> get(@PathVariable String id) {
        return rubriqueCategorieService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RubriqueCategorie> update(@PathVariable String id, @RequestBody RubriqueCategorie newData) {
        return rubriqueCategorieService.update(id, newData)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        rubriqueCategorieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
