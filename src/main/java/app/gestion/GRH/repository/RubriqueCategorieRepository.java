package app.gestion.GRH.repository;

import app.gestion.GRH.model.RubriqueCategorie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RubriqueCategorieRepository extends MongoRepository<RubriqueCategorie, String> {
    List<RubriqueCategorie> findByIdCategorie(String idCategorie);
}
