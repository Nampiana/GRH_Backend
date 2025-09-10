package app.gestion.GRH.repository;

import app.gestion.GRH.model.TurnoverRisk;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TurnoverRiskRepository extends MongoRepository<TurnoverRisk, String> {
    TurnoverRisk findByIdEmployerSociete(String idEmployerSociete);
    List<TurnoverRisk> findByIdEmployerSocieteInOrderByRiskScoreDesc(List<String> ids);
}
