package app.gestion.GRH.repository;

import app.gestion.GRH.model.EmployerSociete;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployerSocieteRepository extends MongoRepository<EmployerSociete, String> {
}
