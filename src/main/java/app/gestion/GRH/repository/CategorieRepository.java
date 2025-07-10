package app.gestion.GRH.repository;

import app.gestion.GRH.model.Categorie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategorieRepository extends MongoRepository<Categorie, String> {
}
