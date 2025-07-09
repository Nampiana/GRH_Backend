package app.gestion.GRH.repository;

import app.gestion.GRH.model.Societe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SocieteRepository extends MongoRepository<Societe, String> {
    Page<Societe> findByNomSocieteContainingIgnoreCase(String nomSociete, Pageable pageable);
}
