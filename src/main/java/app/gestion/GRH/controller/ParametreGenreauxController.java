package app.gestion.GRH.controller;

import app.gestion.GRH.model.ParametreGenreaux;
import app.gestion.GRH.service.ParametreGenreauxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/parametre-genereaux")
@RequiredArgsConstructor
public class ParametreGenreauxController {
    private final ParametreGenreauxService parametreGenreauxService;

    @GetMapping
    public List<ParametreGenreaux> all() {
        return parametreGenreauxService.getAll();
    }

    @PostMapping
    public ParametreGenreaux create(@RequestBody ParametreGenreaux parametreGenreaux) {
        return parametreGenreauxService.create(parametreGenreaux);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametreGenreaux> get(@PathVariable String id) {
        return parametreGenreauxService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParametreGenreaux> update(@PathVariable String id, @RequestBody ParametreGenreaux newParametre) {
        return parametreGenreauxService.update(id, newParametre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        parametreGenreauxService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
