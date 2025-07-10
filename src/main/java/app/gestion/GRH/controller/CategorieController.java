package app.gestion.GRH.controller;

import app.gestion.GRH.model.Categorie;
import app.gestion.GRH.model.Departement;
import app.gestion.GRH.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/categorie")
@RequiredArgsConstructor
public class CategorieController {
    private final CategorieService  categorieService;

    @GetMapping
    public List<Categorie> all() {
        return categorieService.getAll();
    }

    @PostMapping
    public Categorie create(@RequestBody Categorie categorie) {
        return categorieService.create(categorie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorie> get(@PathVariable String id) {
        return categorieService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categorie> update(@PathVariable String id, @RequestBody Categorie newCategorie) {
        return categorieService.update(id, newCategorie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categorieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
