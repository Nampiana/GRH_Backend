package app.gestion.GRH.controller;

import app.gestion.GRH.model.Departement;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping
    public List<Services> all() {
        return serviceService.getAll();
    }

    @PostMapping
    public Services create(@RequestBody Services services) {
        return serviceService.create(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Services> get(@PathVariable String id) {
        return serviceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> update(@PathVariable String id, @RequestBody Services newServices) {
        return serviceService.update(id, newServices)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
