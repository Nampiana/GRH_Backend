// service/RubriquePaieService.java
package app.gestion.GRH.service;

import app.gestion.GRH.model.RubriquePaie;
import app.gestion.GRH.repository.RubriquePaieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RubriquePaieService {
    private final RubriquePaieRepository rubriquePaieRepository;

    public List<RubriquePaie> getAll() {
        return rubriquePaieRepository.findAll();
    }

    public RubriquePaie create(RubriquePaie rubrique) {
        return rubriquePaieRepository.save(rubrique);
    }

    public Optional<RubriquePaie> findById(String id) {
        return rubriquePaieRepository.findById(id);
    }

    public void delete(String id) {
        rubriquePaieRepository.deleteById(id);
    }

    public Optional<RubriquePaie> update(String id, RubriquePaie newRubrique) {
        return rubriquePaieRepository.findById(id).map(r -> {
            r.setCode(newRubrique.getCode());
            r.setNomRubrique(newRubrique.getNomRubrique());
            r.setTypeRubrique(newRubrique.getTypeRubrique());
            r.setOperation(newRubrique.getOperation());
            // ❌ r.setTauxPourcent(...)
            r.setIdParametreGenereaux(newRubrique.getIdParametreGenereaux()); // ✅
            r.setIdSociete(newRubrique.getIdSociete());
            return rubriquePaieRepository.save(r);
        });
    }
}
