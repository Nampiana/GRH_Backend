package app.gestion.GRH.service;

import app.gestion.GRH.model.Pointage;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.model.Societe;
import app.gestion.GRH.repository.PointageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointageService {
    private final PointageRepository pointageRepository;

    public List<Pointage> getAll(){
        return pointageRepository.findAll();
    }

    public Pointage create(Pointage pointage){
        return pointageRepository.save(pointage);
    }

    public Optional<Pointage> findById(String id){
        return pointageRepository.findById(id);
    }

    public void delete(String id){
        pointageRepository.deleteById(id);
    }

    public Optional<Pointage> update(String id, Pointage newPointage){
        return pointageRepository.findById(id).map(p -> {
            p.setDateArriver(newPointage.getDateArriver());
            p.setDateArriver(newPointage.getDateArriver());
            p.setIdEmployerSociete(newPointage.getIdEmployerSociete());
            return pointageRepository.save(p);
        });
    }
}
