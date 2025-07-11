package app.gestion.GRH.controller;

import app.gestion.GRH.dto.EmployerSocieteRequestDTO;
import app.gestion.GRH.model.EmployerSociete;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.service.EmployerSocieteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/employer-societe")
@RequiredArgsConstructor
public class EmployerSocieteController {
    private final EmployerSocieteService employerSocieteService;

    @PostMapping
    public ResponseEntity<EmployerSociete> create(
            @RequestBody EmployerSocieteRequestDTO request
    ) {
        Individu individu = Individu.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .adresse(request.getAdresse())
                .email(request.getEmail())
                .password(request.getPassword())
                .telephone(request.getTelephone())
                .build();

        EmployerSociete created = employerSocieteService.createEmployerSociete(
                request.getEmployerSociete(),
                individu,
                request.getIdSociete(),
                request.getRole()
        );
        return ResponseEntity.ok(created);
    }


    @GetMapping
    public ResponseEntity<List<EmployerSociete>> getAll() {
        return ResponseEntity.ok(employerSocieteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployerSociete> getById(@PathVariable String id) {
        return employerSocieteService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployerSociete> update(@PathVariable String id, @RequestBody EmployerSociete updated) {
        return employerSocieteService.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        employerSocieteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
