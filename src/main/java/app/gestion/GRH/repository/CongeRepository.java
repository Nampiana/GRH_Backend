package app.gestion.GRH.repository;

import app.gestion.GRH.model.Conge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CongeRepository extends MongoRepository<Conge, String> {
}
