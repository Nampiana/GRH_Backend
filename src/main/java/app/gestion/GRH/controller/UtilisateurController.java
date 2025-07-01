package app.gestion.GRH.controller;

import app.gestion.GRH.dto.UtilisateurCreationRequest;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Utilisateur;
import app.gestion.GRH.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
