package app.gestion.GRH.repository;

import app.gestion.GRH.model.MoisPaie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MoisPaieRepository extends MongoRepository<MoisPaie, String> {
    Optional<MoisPaie> findByPeriode(java.time.YearMonth periode);
}
