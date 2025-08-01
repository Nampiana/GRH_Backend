package app.gestion.GRH.repository;

import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Pointage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PointageRepository extends MongoRepository<Pointage, String> {
}
