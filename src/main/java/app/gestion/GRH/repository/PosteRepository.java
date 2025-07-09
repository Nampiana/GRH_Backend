package app.gestion.GRH.repository;

import app.gestion.GRH.model.Departement;
import app.gestion.GRH.model.Poste;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PosteRepository extends MongoRepository<Poste, String> {
}
