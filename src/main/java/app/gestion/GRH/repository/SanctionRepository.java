package app.gestion.GRH.repository;

import app.gestion.GRH.model.Sanction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SanctionRepository extends MongoRepository<Sanction, String> {
    List<Sanction> findByIdEmployer(String idEmployer);
}
