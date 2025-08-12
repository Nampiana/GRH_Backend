package app.gestion.GRH.controller;

import app.gestion.GRH.model.Contrat;
import app.gestion.GRH.service.ContratService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/contrat")
@RequiredArgsConstructor
public class ContratController {

    private final ContratService contratService;

    @GetMapping
    public List<Contrat> all() {
        return contratService.getAll();
    }

    @PostMapping
    public Contrat create(@RequestBody Contrat contrat) {
        return contratService.create(contrat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrat> get(@PathVariable String id) {
        return contratService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrat> update(@PathVariable String id, @RequestBody Contrat newContrat) {
        return contratService.update(id, newContrat)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        contratService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadContrat(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get("uploads/contrat", filename);
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, file.getBytes());
        return ResponseEntity.ok(filename);
    }

    @PutMapping(value = "/update-with-file/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Contrat> updateWithFile(
            @PathVariable String id,
            @RequestPart("contrat") Contrat newContrat,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return (ResponseEntity<Contrat>) contratService.findById(id).map(existing -> {
            try {
                if (file != null && !file.isEmpty()) {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filepath = Paths.get("uploads/contrat", filename);
                    Files.createDirectories(filepath.getParent());
                    Files.write(filepath, file.getBytes());
                    newContrat.setFichierContrat(filename);
                } else {
                    newContrat.setFichierContrat(existing.getFichierContrat());
                }

                if ("CDI".equalsIgnoreCase(newContrat.getTypeContrat())) {
                    newContrat.setDateFin(null);
                }

                newContrat.setId(existing.getId());
                Contrat updated = contratService.create(newContrat); // ou update(...)
                return ResponseEntity.ok(updated);

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // ✅ type Contrat conservé
    }




}
