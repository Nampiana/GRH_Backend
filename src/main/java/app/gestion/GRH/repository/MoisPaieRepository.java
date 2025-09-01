package app.gestion.GRH.repository;

import app.gestion.GRH.model.MoisPaie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.YearMonth;
import java.util.Optional;
import java.util.List;

public interface MoisPaieRepository extends MongoRepository<MoisPaie, String> {
    Optional<MoisPaie> findByPeriode(YearMonth periode); // (legacy)
    Optional<MoisPaie> findByIdSocieteAndPeriode(String idSociete, YearMonth periode); // ⇐ NEW
    List<MoisPaie> findByIdSociete(String idSociete); // utile UI
}
