package app.gestion.GRH.repository;

import app.gestion.GRH.model.Contrat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContratRepository extends MongoRepository<Contrat, String> {
}
