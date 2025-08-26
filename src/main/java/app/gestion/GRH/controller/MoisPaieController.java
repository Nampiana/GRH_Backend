package app.gestion.GRH.controller;

import app.gestion.GRH.model.Departement;
import app.gestion.GRH.model.MoisPaie;
import app.gestion.GRH.service.MoisPaieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/moispaie")
@RequiredArgsConstructor
public class MoisPaieController {
    private final MoisPaieService moisPaieService;

    @GetMapping
    public List<MoisPaie> all() {
        return moisPaieService.getAll();
    }

    @PostMapping
    public MoisPaie create(@RequestBody MoisPaie moisPaie) {
        return moisPaieService.create(moisPaie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoisPaie> get(@PathVariable String id) {
        return moisPaieService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoisPaie> update(@PathVariable String id, @RequestBody MoisPaie newMoisPaie) {
        return moisPaieService.update(id, newMoisPaie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        moisPaieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
