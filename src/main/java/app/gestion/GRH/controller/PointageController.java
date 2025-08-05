package app.gestion.GRH.controller;

import app.gestion.GRH.model.Pointage;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.service.PointageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/pointage")
@RequiredArgsConstructor
public class PointageController {
    private final PointageService pointageService;

    @GetMapping
    public List<Pointage> all() {
        return pointageService.getAll();
    }

    @PostMapping
    public Pointage create(@RequestBody Pointage pointage) {
        return pointageService.create(pointage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pointage> get(@PathVariable String id) {
        return pointageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pointage> update(@PathVariable String id, @RequestBody Pointage newServicesPointage) {
        return pointageService.update(id, newServicesPointage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pointageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
