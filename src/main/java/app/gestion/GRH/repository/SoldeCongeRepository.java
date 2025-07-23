package app.gestion.GRH.repository;

import app.gestion.GRH.model.SoldeConge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SoldeCongeRepository extends MongoRepository<SoldeConge, String> {
    SoldeConge findByIdEmployerSociete(String idEmployerSociete);
}
