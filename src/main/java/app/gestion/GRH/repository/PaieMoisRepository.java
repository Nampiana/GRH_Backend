package app.gestion.GRH.repository;

import app.gestion.GRH.model.PaieMois;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaieMoisRepository extends MongoRepository<PaieMois, String> {
    List<PaieMois> findByIdEmployerAndMoisPaieId(String idEmployer, String moisPaieId);
    void deleteByIdEmployerAndMoisPaieId(String idEmployer, String moisPaieId);
}
