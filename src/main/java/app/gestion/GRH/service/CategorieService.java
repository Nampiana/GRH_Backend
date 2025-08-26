package app.gestion.GRH.service;

import app.gestion.GRH.model.Categorie;
import app.gestion.GRH.model.Departement;
import app.gestion.GRH.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorieService {
    private final CategorieRepository categorieRepository;

    public List<Categorie> getAll(){
        return categorieRepository.findAll();
    }

    public Categorie create(Categorie categorie){
        return categorieRepository.save(categorie);
    }

    public Optional<Categorie> findById(String id){
        return categorieRepository.findById(id);
    }

    public void delete(String id){
        categorieRepository.deleteById(id);
    }

    public Optional<Categorie> update(String id, Categorie newCategorie){
        return categorieRepository.findById(id).map(c -> {
            c.setNomCategorie(newCategorie.getNomCategorie());
            c.setIdSociete(newCategorie.getIdSociete());
            return categorieRepository.save(c);
        });
    }
}
