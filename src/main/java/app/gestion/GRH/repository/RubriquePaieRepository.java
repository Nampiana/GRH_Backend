package app.gestion.GRH.repository;

import app.gestion.GRH.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RubriquePaieRepository extends MongoRepository<RubriquePaie, String> {
    Optional<RubriquePaie> findByCode(String code);
}
