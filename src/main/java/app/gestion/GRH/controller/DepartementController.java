package app.gestion.GRH.controller;

import app.gestion.GRH.model.Departement;
import app.gestion.GRH.service.DepartementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/departement")
@RequiredArgsConstructor
public class DepartementController {
    private final DepartementService departementService;

    @GetMapping
    public List<Departement> all() {
        return departementService.getAll();
    }

    @PostMapping
    public Departement create(@RequestBody Departement departement) {
        return departementService.create(departement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> get(@PathVariable String id) {
        return departementService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departement> update(@PathVariable String id, @RequestBody Departement newDepartement) {
        return departementService.update(id, newDepartement)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        departementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
