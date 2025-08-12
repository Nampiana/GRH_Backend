package app.gestion.GRH.controller;

import app.gestion.GRH.model.Sanction;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.service.SanctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sanction")
@RequiredArgsConstructor
public class SanctionController {
    private final SanctionService sanctionService;

    @GetMapping
    public List<Sanction> all() {
        return sanctionService.getAll();
    }

    @PostMapping
    public Sanction create(@RequestBody Sanction sanction) {
        return sanctionService.create(sanction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sanction> get(@PathVariable String id) {
        return sanctionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sanction> update(@PathVariable String id, @RequestBody Sanction newSanction) {
        return sanctionService.update(id, newSanction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sanctionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
