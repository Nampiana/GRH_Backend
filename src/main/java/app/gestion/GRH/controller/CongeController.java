package app.gestion.GRH.controller;

import app.gestion.GRH.model.Conge;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.service.CongeService;
import app.gestion.GRH.service.IndividuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/conge")
@RequiredArgsConstructor
public class CongeController {
    private final CongeService congeService;

    @GetMapping
    public List<Conge> all() {
        return congeService.getAll();
    }

    @PostMapping
    public Conge create(@RequestBody Conge conge) {
        return congeService.create(conge);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conge> get(@PathVariable String id) {
        return congeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conge> update(@PathVariable String id, @RequestBody Conge newConge) {
        return congeService.update(id, newConge)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        congeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-justification")
    public ResponseEntity<String> uploadJustificatif(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        try {
            // 🔒 Utiliser chemin absolu
            String uploadsDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "justificationConge" + File.separator;
            File dir = new File(uploadsDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Impossible de créer le dossier d'upload");
            }

            // nom de fichier unique
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf('.'));
            String filename = UUID.randomUUID() + extension;

            File dest = new File(uploadsDir + filename);
            file.transferTo(dest);

            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erreur lors de l'upload");
        }
    }


}
