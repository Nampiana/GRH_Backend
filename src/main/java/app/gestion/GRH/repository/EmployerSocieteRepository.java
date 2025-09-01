package app.gestion.GRH.repository;

import app.gestion.GRH.model.EmployerSociete;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EmployerSocieteRepository extends MongoRepository<EmployerSociete, String> {
    Optional<EmployerSociete> findByIdUtilisateur(String idUtilisateur);
    Optional<EmployerSociete> findByIdIndividue(String idIndividue);

    List<EmployerSociete> findByIdSociete(String idSociete); // ⇐ NEW
    List<EmployerSociete> findByIdSocieteAndDateDebaucheIsNull(String idSociete); // ⇐ NEW (employés actifs)
}
