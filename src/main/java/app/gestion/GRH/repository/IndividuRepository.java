package app.gestion.GRH.repository;

import app.gestion.GRH.model.Individu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IndividuRepository extends MongoRepository<Individu, String> {
    Optional<Individu> findByEmail(String email);
    boolean existsByEmail(String email);
}
