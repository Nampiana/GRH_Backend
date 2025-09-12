package app.gestion.GRH.repository;

import app.gestion.GRH.model.Utilisateur;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {
    List<Utilisateur> findByIdSocieteAndRolesAndEtat(String idSociete, Integer roles, Integer etat);

    // (optionnel) si besoin d'une variante sans etat
    List<Utilisateur> findByIdSocieteAndRoles(String idSociete, Integer roles);
}
