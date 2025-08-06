package app.gestion.GRH.controller;

import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Poste;
import app.gestion.GRH.repository.IndividuRepository;
import app.gestion.GRH.service.IndividuService;
import app.gestion.GRH.service.PosteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/individu")
@RequiredArgsConstructor
public class IndividuController {
    private final IndividuService individuService;
    private final IndividuRepository individuRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping
    public List<Individu> all() {
        return individuService.getAll();
    }

    @PostMapping
    public Individu create(@RequestBody Individu poste) {
        return individuService.create(poste);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Individu> get(@PathVariable String id) {
        return individuService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Individu> update(@PathVariable String id, @RequestBody Individu newIndividu) {
        return individuService.update(id, newIndividu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        individuService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody Map<String, String> body) {
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword");

        Optional<Individu> optionalIndividu = individuRepository.findById(id);
        if (optionalIndividu.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }

        Individu individu = optionalIndividu.get();

        if (!passwordEncoder.matches(currentPassword, individu.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mot de passe actuel incorrect");
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les nouveaux mots de passe ne correspondent pas");
        }

        individu.setPassword(passwordEncoder.encode(newPassword));
        individuRepository.save(individu);

        return ResponseEntity.ok("Mot de passe mis à jour avec succès");
    }

}
