package app.gestion.GRH.repository;

import app.gestion.GRH.model.Conge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CongeRepository extends MongoRepository<Conge, String> {
    List<Conge> findByIdEmployerSociete(String idEmployerSociete);
}
