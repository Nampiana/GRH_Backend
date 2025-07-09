package app.gestion.GRH.repository;

import app.gestion.GRH.model.Services;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<Services, String> {
}
