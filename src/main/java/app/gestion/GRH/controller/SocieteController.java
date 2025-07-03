package app.gestion.GRH.controller;

import app.gestion.GRH.model.Societe;
import app.gestion.GRH.service.SocieteService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/societe")
@RequiredArgsConstructor
public class SocieteController {
    private final SocieteService societeService;

    @GetMapping
    public List<Societe> all() {
        return societeService.getAll();
    }

    @PostMapping
    public Societe create(@RequestBody Societe societe){
        return societeService.create(societe);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Societe> findById(@PathVariable String id){
        return societeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Societe> update(@PathVariable String id, @RequestBody Societe newSociete){
        return societeService.update(id, newSociete)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            societeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
