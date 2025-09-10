package app.gestion.GRH.repository;

import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Pointage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PointageRepository extends MongoRepository<Pointage, String> {
    List<Pointage> findByIdEmployerSociete(String idEmployerSociete);
}
