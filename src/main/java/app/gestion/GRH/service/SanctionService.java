package app.gestion.GRH.service;

import app.gestion.GRH.model.Sanction;
import app.gestion.GRH.repository.SanctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SanctionService {
    private final SanctionRepository sanctionRepository;
    private final SanctionSseService sanctionSseService;

    public List<Sanction> getAll() {
        return sanctionRepository.findAll();
    }

    public Optional<Sanction> findById(String id) {
        return sanctionRepository.findById(id);
    }

    public Sanction create(Sanction sanction) {
        Sanction saved = sanctionRepository.save(sanction);
        // Notifier immédiatement l’employé concerné
        sanctionSseService.publish(saved);
        return saved;
    }

    public Optional<Sanction> update(String id, Sanction newSanction) {
        return sanctionRepository.findById(id).map(s -> {
            s.setIdEmployer(newSanction.getIdEmployer());
            s.setTypeSanction(newSanction.getTypeSanction());
            s.setMotif(newSanction.getMotif());
            s.setDateSanction(newSanction.getDateSanction());
            Sanction updated = sanctionRepository.save(s);
            // (Optionnel) notifier aussi en cas de modif
            sanctionSseService.publish(updated);
            return updated;
        });
    }

    public void delete(String id) {
        sanctionRepository.deleteById(id);
    }
}
