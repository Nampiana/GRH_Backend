package app.gestion.GRH.repository;

import app.gestion.GRH.model.Sanction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SanctionRepository extends MongoRepository<Sanction, String> {
}
