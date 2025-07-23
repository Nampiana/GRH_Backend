package app.gestion.GRH.service;

import app.gestion.GRH.model.Conge;
import app.gestion.GRH.model.SoldeConge;
import app.gestion.GRH.repository.SoldeCongeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoldeCongeService {
    private final SoldeCongeRepository soldeCongeRepository;

    public List<SoldeConge> getAll(){
        return soldeCongeRepository.findAll();
    }

    public SoldeConge create(SoldeConge soldeConge){
        return soldeCongeRepository.save(soldeConge);
    }

    public Optional<SoldeConge> findById(String id){
        return soldeCongeRepository.findById(id);
    }

    public void delete(String id){
        soldeCongeRepository.deleteById(id);
    }

    public Optional<SoldeConge> update(String id, SoldeConge newSoldeConge) {
        return soldeCongeRepository.findById(id).map(s -> {
            s.setIdEmployerSociete(newSoldeConge.getIdEmployerSociete());
            s.setSolde(newSoldeConge.getSolde());
            return soldeCongeRepository.save(s);
        });
    }

    public SoldeConge getByEmployerId(String idEmployerSociete) {
        return soldeCongeRepository.findByIdEmployerSociete(idEmployerSociete);
    }
}
