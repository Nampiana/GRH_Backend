package app.gestion.GRH.repository;

import app.gestion.GRH.model.EmployerSociete;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployerSocieteRepository extends MongoRepository<EmployerSociete, String> {
    Optional<EmployerSociete> findByIdUtilisateur(String idUtilisateur);
}
