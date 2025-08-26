package app.gestion.GRH.repository;

import app.gestion.GRH.model.ParametreGenreaux;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParametreGenreauxRepository extends MongoRepository<ParametreGenreaux, String> {
}
