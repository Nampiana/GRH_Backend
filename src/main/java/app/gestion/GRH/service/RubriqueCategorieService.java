package app.gestion.GRH.service;

import app.gestion.GRH.model.RubriqueCategorie;
import app.gestion.GRH.repository.RubriqueCategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RubriqueCategorieService {
    private final RubriqueCategorieRepository rubriqueCategorieRepository;

    public List<RubriqueCategorie> getAll() {
        return rubriqueCategorieRepository.findAll();
    }

    public RubriqueCategorie create(RubriqueCategorie rubriqueCategorie) {
        return rubriqueCategorieRepository.save(rubriqueCategorie);
    }

    public Optional<RubriqueCategorie> findById(String id) {
        return rubriqueCategorieRepository.findById(id);
    }

    public void delete(String id) {
        rubriqueCategorieRepository.deleteById(id);
    }

    public Optional<RubriqueCategorie> update(String id, RubriqueCategorie newData) {
        return rubriqueCategorieRepository.findById(id).map(rc -> {
            rc.setIdCategorie(newData.getIdCategorie());
            rc.setIdRubriquePaie(newData.getIdRubriquePaie());
            return rubriqueCategorieRepository.save(rc);
        });
    }
}
