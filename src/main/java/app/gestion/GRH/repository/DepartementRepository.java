package app.gestion.GRH.repository;

import app.gestion.GRH.model.Departement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartementRepository extends MongoRepository<Departement, String> {
}
