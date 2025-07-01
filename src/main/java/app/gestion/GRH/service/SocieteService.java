package app.gestion.GRH.service;

import app.gestion.GRH.model.Societe;
import app.gestion.GRH.repository.SocieteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocieteService {
    private final SocieteRepository societeRepository;

    public List<Societe> getAll(){
        return societeRepository.findAll();
    }

    public Societe create(Societe societe){
        return societeRepository.save(societe);
    }

    public Optional<Societe> findById(String id){
        return societeRepository.findById(id);
    }

    public void delete(String id){
        societeRepository.deleteById(id);
    }

    public Optional<Societe> update(String id, Societe newSociete){
        return societeRepository.findById(id).map( s -> {
            s.setNom_societe(newSociete.getNom_societe());
            return societeRepository.save(s);
        });
    }
}
