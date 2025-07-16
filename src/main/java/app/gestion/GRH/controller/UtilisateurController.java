package app.gestion.GRH.controller;

import app.gestion.GRH.dto.UtilisateurCreationRequest;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.model.Utilisateur;
import app.gestion.GRH.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @PostMapping("/create_user")
    public ResponseEntity<Utilisateur> createWithIndividu(@RequestBody UtilisateurCreationRequest request) {
        Individu individu = Individu.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .adresse(request.getAdresse())
                .email(request.getEmail())
                .password(request.getPassword())
                .telephone(request.getTelephone())
                .build();

        Utilisateur utilisateur = utilisateurService.createWithIndividu(individu, request.getRoles(), request.getIdSociete());
        return ResponseEntity.ok(utilisateur);
    }

    @GetMapping
    public List<Utilisateur> all() {
        return utilisateurService.getAll();
    }

    @PostMapping
    public Utilisateur create(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.create(utilisateur);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> get(@PathVariable String id) {
        return utilisateurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable String id, @RequestBody Utilisateur newUtilisateur) {
        return utilisateurService.update(id, newUtilisateur)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
