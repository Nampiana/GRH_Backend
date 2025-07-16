package app.gestion.GRH.service;

import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.model.Utilisateur;
import app.gestion.GRH.repository.IndividuRepository;
import app.gestion.GRH.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final IndividuRepository individuRepository;
    private final PasswordEncoder passwordEncoder;

    public Utilisateur createWithIndividu(Individu individu, Integer roles, String idSociete) {

        if (individuRepository.existsByEmail(individu.getEmail())) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        individu.setPassword(passwordEncoder.encode(individu.getPassword()));
        // 1. Sauvegarde de l'individu
        Individu savedIndividu = individuRepository.save(individu);

        // 2. Création de l'utilisateur lié
        Utilisateur utilisateur = Utilisateur.builder()
                .idIndividu(savedIndividu.getId())
                .roles(roles)
                .idSociete(idSociete)
                .etat(1)
                .build();

        return utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> getAll(){
        return utilisateurRepository.findAll();
    }

    public Utilisateur create(Utilisateur utilisateur){
        return utilisateurRepository.save(utilisateur);
    }

    public Optional<Utilisateur> findById(String id){
        return utilisateurRepository.findById(id);
    }

    public void delete(String id){
        utilisateurRepository.deleteById(id);
    }

    public Optional<Utilisateur> update(String id, Utilisateur newUtilisateur){
        return utilisateurRepository.findById(id).map(u -> {
            u.setIdIndividu(newUtilisateur.getIdIndividu());
            u.setEtat(newUtilisateur.getEtat());
            u.setRoles(newUtilisateur.getRoles());
            u.setIdSociete(newUtilisateur.getIdSociete());
            return utilisateurRepository.save(u);
        });
    }
}
