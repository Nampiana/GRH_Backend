package app.gestion.GRH.service;

import app.gestion.GRH.model.Conge;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.repository.CongeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CongeService {
    private final CongeRepository congeRepository;

    public List<Conge> getAll(){
        return congeRepository.findAll();
    }

    public Conge create(Conge conge){
        return congeRepository.save(conge);
    }

    public Optional<Conge> findById(String id){
        return congeRepository.findById(id);
    }

    public void delete(String id){
        congeRepository.deleteById(id);
    }

    public Optional<Conge> update(String id, Conge newConge) {
        return congeRepository.findById(id).map(c -> {
            c.setIdEmployerSociete(newConge.getIdEmployerSociete());
            c.setDateDebut(newConge.getDateDebut());
            c.setDateFin(newConge.getDateFin());
            c.setMotif(newConge.getMotif());
            c.setStatut(newConge.getStatut());
            c.setDuree(newConge.getDuree());
            return congeRepository.save(c);
        });
    }
}
