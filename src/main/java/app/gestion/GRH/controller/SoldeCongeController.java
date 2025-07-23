package app.gestion.GRH.controller;

import app.gestion.GRH.model.Conge;
import app.gestion.GRH.model.SoldeConge;
import app.gestion.GRH.service.SoldeCongeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/soldeconge")
@RequiredArgsConstructor
public class SoldeCongeController {
    private final SoldeCongeService  soldeCongeService;

    @GetMapping
    public List<SoldeConge> all() {
        return soldeCongeService.getAll();
    }

    @PostMapping
    public SoldeConge create(@RequestBody SoldeConge soldeConge) {
        return soldeCongeService.create(soldeConge);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoldeConge> get(@PathVariable String id) {
        return soldeCongeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoldeConge> update(@PathVariable String id, @RequestBody SoldeConge newSoldeConge) {
        return soldeCongeService.update(id, newSoldeConge)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        soldeCongeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employe/{id}")
    public SoldeConge getSoldeByEmployer(@PathVariable String id) {
        return soldeCongeService.getByEmployerId(id);
    }
}
